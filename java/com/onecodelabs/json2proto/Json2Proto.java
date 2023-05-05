package com.onecodelabs.json2proto;

import com.google.common.base.CaseFormat;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.onecodelabs.common.AssertUtils;
import com.onecodelabs.common.ProtoUtils;
import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;
import com.onecodelabs.flags.Flags;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Json2Proto {

    @FlagSpec(name = "json_file", description = "")
    public static Flag<String> jsonFile = Flag.empty();

    @FlagSpec(name = "proto_file", description = "")
    public static Flag<String> protoFile = Flag.empty();

    @FlagSpec(name = "proto_message", description = "")
    public static Flag<String> protoMessage = Flag.empty();

    public static String getFileName(String filePath) {
        String ans = filePath.substring(filePath.lastIndexOf("/") + 1);
        ans = ans.substring(0, ans.indexOf("."));
        return ans;
    }

    public static String getOuterClass(DescriptorProtos.FileDescriptorProto fileDescriptorProto) {
        if (fileDescriptorProto.getOptions().getJavaMultipleFiles()) {
            return "";
        }
        if (fileDescriptorProto.getOptions().hasJavaOuterClassname()) {
            return fileDescriptorProto.getOptions().getJavaOuterClassname();
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getFileName(fileDescriptorProto.getName()));
    }

    public static String getPackage(DescriptorProtos.FileDescriptorProto fileDescriptorProto) {
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

    public static void main(String[] args) throws Exception {
        Flags.parse(args);
        AssertUtils.assumeTrue("Didn't find required flag --json_file", jsonFile.get() != null);
        AssertUtils.assumeTrue("Didn't find required flag --proto_file", protoFile.get() != null);
        AssertUtils.assumeTrue("Didn't find required flag --proto_message", protoMessage.get() != null);
        String jsonFilePath = jsonFile.getNotNull();
        String protoFilePath = protoFile.getNotNull();
        String message = protoMessage.getNotNull();

        String jsonContents = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        byte[] protoContents = Files.readAllBytes(Paths.get(protoFilePath));

        DescriptorProtos.FileDescriptorSet fileDescriptorSet =
                DescriptorProtos.FileDescriptorSet.parseFrom(protoContents);
        // TODO: find FileDescriptorProto based on input proto file name
        DescriptorProtos.FileDescriptorProto fileDescriptorProto = fileDescriptorSet.getFile(0);
        Message proto = ProtoUtils.getDefaultInstance(getClassName(fileDescriptorProto, message));
        Message.Builder builder = proto.newBuilderForType();
        JsonFormat.parser().merge(jsonContents, builder);
        System.out.println(builder.build().toString());
    }
}
