package com.onecodelabs.common;

import com.google.common.base.CaseFormat;
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

    public static Message getDefaultInstance(DescriptorProtos.FileDescriptorProto fileDescriptorProto, String message)
            throws Exception {
        return getDefaultInstance(getClassName(fileDescriptorProto, message));
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

    private static String getFileName(String filePath) {
        String ans = filePath.substring(filePath.lastIndexOf("/") + 1);
        ans = ans.substring(0, ans.indexOf("."));
        return ans;
    }

    private static String getOuterClass(DescriptorProtos.FileDescriptorProto fileDescriptorProto) {
        if (fileDescriptorProto.getOptions().getJavaMultipleFiles()) {
            return "";
        }
        if (fileDescriptorProto.getOptions().hasJavaOuterClassname()) {
            return fileDescriptorProto.getOptions().getJavaOuterClassname();
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getFileName(fileDescriptorProto.getName()));
    }

    private static String getPackage(DescriptorProtos.FileDescriptorProto fileDescriptorProto) {
        if (fileDescriptorProto.getOptions().hasJavaPackage()) {
            return fileDescriptorProto.getOptions().getJavaPackage();
        }
        return fileDescriptorProto.getPackage();
    }

    public static String getClassName(DescriptorProtos.FileDescriptorProto fileDescriptorProto, String message) {
        StringBuilder sb = new StringBuilder(getPackage(fileDescriptorProto));
        String outerClass = getOuterClass(fileDescriptorProto);
        if (!outerClass.isEmpty()) {
            sb.append("." + outerClass);
        }
        sb.append("." + message);
        return sb.toString();
    }
}
