package com.onecodelabs.common;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Message;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtoUtils {

    public static Message getDefaultInstance(String className) throws Exception {
        Class<?> clazz = ReflectionUtils.findClassByName(className);
        Method method = clazz.getDeclaredMethod("getDefaultInstance");
        return (Message) method.invoke(clazz);
    }

    public static Map<String, DescriptorProtos.FileDescriptorProto> loadDescriptors(List<String> protoLibs)
            throws IOException {
        Map<String, DescriptorProtos.FileDescriptorProto> descriptors = new HashMap<>();

        for (String protoLib : protoLibs) {
            byte[] protoLibFileContents = Files.readAllBytes(Paths.get(protoLib));
            DescriptorProtos.FileDescriptorSet fileDescriptorSet =
                    DescriptorProtos.FileDescriptorSet.parseFrom(protoLibFileContents);
            for (DescriptorProtos.FileDescriptorProto fileDescriptorProto : fileDescriptorSet.getFileList()) {
                if (descriptors.containsKey(fileDescriptorProto.getPackage())) {
                    throw new IllegalStateException(
                            "Multiple protos declaring the same package: " + fileDescriptorProto.getPackage());
                }
                descriptors.put(fileDescriptorProto.getPackage(), fileDescriptorProto);
            }
        }

        return descriptors;
    }
}
