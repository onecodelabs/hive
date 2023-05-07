package com.onecodelabs.serverframework.request;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServiceDescriptor;
import io.grpc.protobuf.ProtoFileDescriptorSupplier;
import io.grpc.protobuf.ProtoMethodDescriptorSupplier;
import io.grpc.protobuf.ProtoServiceDescriptorSupplier;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.ServerCalls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RpcDispatcherModule extends AbstractModule {

    private final Descriptors.ServiceDescriptor descriptor;

    private RpcDispatcherModule(Descriptors.ServiceDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public static RpcDispatcherModule forService(Descriptors.ServiceDescriptor descriptor) {
        return new RpcDispatcherModule(descriptor);
    }

    private static Optional<Method> getRpcMethod(Action action) {
        for (Method method : action.getClass().getDeclaredMethods()) {
            if (method.getName().equals("handleRequest")) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    private static Descriptors.Descriptor getDescriptor(Class<? extends Message> messageClass) {
        try {
            Method method = messageClass.getMethod("getDescriptor");
            return (Descriptors.Descriptor) method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodDescriptor toGrpcMethodDescriptor(Descriptors.MethodDescriptor methodDescriptor) {
        Message requestTypeDefaultInstance = null;
        Message responseTypeDefaultInstance = null;
        try {
            requestTypeDefaultInstance = com.onecodelabs.common.ProtoUtils.getDefaultInstance(
                    methodDescriptor.getInputType().getFile().toProto(),
                    methodDescriptor.getInputType().getName()
            );
            responseTypeDefaultInstance = com.onecodelabs.common.ProtoUtils.getDefaultInstance(
                    methodDescriptor.getOutputType().getFile().toProto(),
                    methodDescriptor.getOutputType().getName()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String fullMethodName = String.format("%s.%s/%s", methodDescriptor.getFile().getPackage(),
                methodDescriptor.getService().getName(), methodDescriptor.getName());
        return MethodDescriptor.<Message, Message>newBuilder()
                .setType(MethodDescriptor.MethodType.UNARY)
                .setSchemaDescriptor(new MethodDescriptorSupplier(methodDescriptor))
                .setFullMethodName(fullMethodName)
                .setSampledToLocalTracing(true)
                .setRequestMarshaller(ProtoUtils.marshaller(requestTypeDefaultInstance))
                .setResponseMarshaller(ProtoUtils.marshaller(responseTypeDefaultInstance))
                .build();
    }

    private static ServiceDescriptor toGrpcServiceDescriptor(Descriptors.ServiceDescriptor serviceDescriptor,
                                                             Map<Descriptors.MethodDescriptor, MethodDescriptor> mappings) {
        String fullServiceName = String.format("%s.%s", serviceDescriptor.getFile().getPackage(), serviceDescriptor.getName());
        ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(fullServiceName)
                .setSchemaDescriptor(new DescriptorSupplier(serviceDescriptor));
        serviceDescriptor.getMethods().stream().map(mappings::get)
                .forEach(builder::addMethod);
        return builder.build();
    }

    @ProvidesIntoSet
    BindableService provideBindableService(Map<String, Action> actionMap) {

        // Same instance of MethodDescriptor needs to be used in multiple places
        Map<Descriptors.MethodDescriptor, MethodDescriptor> methodDescriptorMappings = new HashMap<>();
        for (Descriptors.MethodDescriptor methodDescriptor : descriptor.getMethods()) {
            methodDescriptorMappings.put(methodDescriptor, toGrpcMethodDescriptor(methodDescriptor));
        }

        String serviceName = descriptor.getName();
        ServerServiceDefinition.Builder definition =
                ServerServiceDefinition.builder(toGrpcServiceDescriptor(descriptor, methodDescriptorMappings));

        for (Descriptors.MethodDescriptor methodDescriptor : descriptor.getMethods()) {
            String methodName = methodDescriptor.getName();
            String actionKey = String.format("/%s.%s", serviceName, methodName);

            if (!actionMap.containsKey(actionKey)) {
                throw new IllegalStateException(String.format("Missing Action binding: %s", actionKey));
            }
            Action action = actionMap.get(actionKey);
            Optional<Method> rpcMethod = getRpcMethod(action);
            if (!rpcMethod.isPresent()) {
                throw new IllegalStateException(
                        String.format("Action %s doesn't have a declared method handleRequest", actionKey));
            }

            Class<?> requestType = rpcMethod.get().getParameterTypes()[0];
            Class<?> responseType = rpcMethod.get().getReturnType();
            if (!Message.class.isAssignableFrom(requestType)) {
                throw new IllegalStateException(
                        String.format("Request type %s in method %s in action %s is not of a proto message",
                                requestType.getName(), rpcMethod.get().getName(), actionKey));
            }

            if (!Message.class.isAssignableFrom(responseType)) {
                throw new IllegalStateException(
                        String.format("Response type %s in method %s in action %s is not of a proto message",
                                responseType.getName(), rpcMethod.get().getName(), actionKey));
            }

            Descriptors.Descriptor requestDescriptor = getDescriptor((Class<? extends Message>) requestType);
            Descriptors.Descriptor responseDescriptor = getDescriptor((Class<? extends Message>) responseType);
            Descriptors.Descriptor inputDescriptor = methodDescriptor.getInputType();
            Descriptors.Descriptor outputDescriptor = methodDescriptor.getOutputType();

            if (!requestDescriptor.getName().equals(inputDescriptor.getName())) {
                throw new IllegalStateException(
                        String.format("Request type is %s but expected %s", requestDescriptor.getName(),
                                inputDescriptor.getName()));
            }
            if (!requestDescriptor.getFile().getPackage().equals(inputDescriptor.getFile().getPackage())) {
                throw new IllegalStateException(String.format("Request type %s has proto package %s but expected %s",
                        requestDescriptor.getName(), requestDescriptor.getFile().getPackage(),
                        inputDescriptor.getFile().getPackage()));
            }
            if (!responseDescriptor.getName().equals(outputDescriptor.getName())) {
                throw new IllegalStateException(
                        String.format("Response type is %s but expected %s", responseDescriptor.getName(),
                                outputDescriptor.getName()));
            }
            if (!responseDescriptor.getFile().getPackage().equals(outputDescriptor.getFile().getPackage())) {
                throw new IllegalStateException(String.format("Response type %s has proto package %s but expected %s",
                        responseDescriptor.getName(), responseDescriptor.getFile().getPackage(),
                        outputDescriptor.getFile().getPackage()));
            }

            definition.addMethod(methodDescriptorMappings.get(methodDescriptor), ServerCalls.asyncUnaryCall(
                    (ServerCalls.UnaryMethod) (request, responseObserver) -> {
                        try {
                            responseObserver.onNext(action.handleRequest(request));
                        } catch (Exception e) {
                            responseObserver.onError(e);
                        }
                        responseObserver.onCompleted();
                    }));
        }

        return definition::build;
    }

    @Override

    protected void configure() {
    }

    private static class DescriptorSupplier implements ProtoFileDescriptorSupplier,
            ProtoServiceDescriptorSupplier {

        private final Descriptors.ServiceDescriptor serviceDescriptor;

        public DescriptorSupplier(Descriptors.ServiceDescriptor serviceDescriptor) {
            this.serviceDescriptor = serviceDescriptor;
        }

        @Override
        public Descriptors.FileDescriptor getFileDescriptor() {
            return serviceDescriptor.getFile();
        }

        @Override
        public Descriptors.ServiceDescriptor getServiceDescriptor() {
            return serviceDescriptor;
        }
    }

    private static class MethodDescriptorSupplier implements ProtoMethodDescriptorSupplier {

        private final Descriptors.MethodDescriptor methodDescriptor;

        public MethodDescriptorSupplier(Descriptors.MethodDescriptor methodDescriptor) {
            this.methodDescriptor = methodDescriptor;
        }

        @Override
        public Descriptors.MethodDescriptor getMethodDescriptor() {
            return methodDescriptor;
        }

        @Override
        public Descriptors.ServiceDescriptor getServiceDescriptor() {
            return methodDescriptor.getService();
        }

        @Override
        public Descriptors.FileDescriptor getFileDescriptor() {
            return methodDescriptor.getFile();
        }
    }
}
