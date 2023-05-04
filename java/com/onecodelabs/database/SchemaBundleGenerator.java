package com.onecodelabs.database;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.onecodelabs.common.ProtoUtils;
import com.onecodelabs.common.ReflectionUtils;
import com.onecodelabs.flags.Flags;
import database.*;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.onecodelabs.common.ProtoUtils.importProto;

public class SchemaBundleGenerator {

    // TODO: refactor so it parses proto files directly!
    public static void main(String[] args) throws Exception {
        Flags.parseSystemProperties();
        String fileContents = new String(Files.readAllBytes(Paths.get(DatabaseFlags.schemaPath.getNotNull())));
        Schema schema = TextFormat.parse(fileContents, Schema.class);

        SchemaBundle.Builder bundle = SchemaBundle.newBuilder();
        SchemaBundleMetadata.Builder metadata = SchemaBundleMetadata.newBuilder();
        bundle.setSchema(schema);

        for (String protoImport : schema.getProtoImportList()) {
            Message message = importProto(protoImport);
            ProtoMetadata.Builder protoMetadata = ProtoMetadata.newBuilder().setProtoImport(protoImport);
            for (String fieldName : ProtoUtils.fieldNames(message)) {
                Descriptors.FieldDescriptor descriptor = ProtoUtils.getFieldDescriptor(message, fieldName).get();

                protoMetadata.addProtoField(ProtoField.newBuilder()
                        .setFieldName(fieldName)
                        .setType(descriptor.getType().toString())
                        .build());
            }
            metadata.addProtoMetadata(protoMetadata.build());
        }
        bundle.setMetadata(metadata.build());

        System.out.println(bundle.build().toString());
    }
}
