package com.onecodelabs.common;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProtoUtils {

    public static final Optional<Descriptors.FieldDescriptor> getFieldDescriptor(Message message, String fieldName) {
        return message.getDescriptorForType().getFields().stream().filter(fd -> fieldName.equals(fd.getName()))
                .findFirst();
    }

    public static final List<String> fieldNames(Message message) {
        return message.getDescriptorForType().getFields().stream().map(Descriptors.FieldDescriptor::getName)
                .collect(Collectors.toList());
    }

    public static final List<String> getPrimitiveFields(Message message) {
        return fieldNames(message).stream().filter(name -> isPrimitiveType(getFieldDescriptor(message, name).get()
                .getType())).collect(Collectors.toList());
    }

    public static final boolean isPrimitiveType(Descriptors.FieldDescriptor.Type type) {
        return !Descriptors.FieldDescriptor.Type.MESSAGE.equals(type);
    }
}
