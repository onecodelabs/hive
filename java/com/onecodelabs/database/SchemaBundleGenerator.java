package com.onecodelabs.database;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.TextFormat;
import com.onecodelabs.common.ProtoUtils;
import com.onecodelabs.flags.Flags;
import database.ProtoField;
import database.ProtoMetadata;
import database.Schema;
import database.SchemaBundle;
import database.SchemaBundleMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;

import static com.onecodelabs.database.Constants.PATTERN_IMPORT;

public class SchemaBundleGenerator {

    public static void main(String[] args) throws Exception {
        Flags.parse(args);

        String fileContents = new String(Files.readAllBytes(Paths.get(DatabaseFlags.schemaPath.getNotNull())));
        Schema schema = TextFormat.parse(fileContents, Schema.class);

        Map<String, DescriptorProtos.FileDescriptorProto> descriptors =
                ProtoUtils.loadDescriptors(DatabaseFlags.protoLibs.getNotNull());

        SchemaBundle.Builder bundle = SchemaBundle.newBuilder();
        SchemaBundleMetadata.Builder metadata = SchemaBundleMetadata.newBuilder();
        bundle.setSchema(schema);

        for (String protoImport : schema.getProtoImportList()) {
            Matcher m = PATTERN_IMPORT.matcher(protoImport);
            if (!m.matches()) {
                throw new IllegalStateException(
                        String.format("Proto import %s does not match pattern %s", protoImport, PATTERN_IMPORT));
            }
            String protoPackage = m.group(1);
            String protoMessage = m.group(2);
            if (!descriptors.containsKey(protoPackage)) {
                throw new IllegalStateException(
                        String.format("Proto library \"%s\" is missing, only found %s", protoImport,
                                descriptors.keySet()));
            }
            DescriptorProtos.FileDescriptorProto fileDescriptorProto = descriptors.get(protoPackage);

            ProtoMetadata.Builder protoMetadata = ProtoMetadata.newBuilder().setProtoImport(protoImport);
            // TODO: see if the entire tree of messages can be traversed
            for (DescriptorProtos.DescriptorProto descriptorProto : fileDescriptorProto.getMessageTypeList()) {
                if (!descriptorProto.getName().equals(protoMessage)) {
                    continue;
                }
                for (DescriptorProtos.FieldDescriptorProto fieldDescriptorProto : descriptorProto.getFieldList()) {
                    protoMetadata.addProtoField(ProtoField.newBuilder()
                            .setFieldName(fieldDescriptorProto.getName())
                            .setType(fieldDescriptorProto.getType().name())
                            .build());
                }
            }

            metadata.addProtoMetadata(protoMetadata.build());
        }
        bundle.setMetadata(metadata.build());

        System.out.println(bundle.build().toString());
    }
}
