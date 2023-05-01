package com.onecodelabs.database;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.onecodelabs.flags.Flags;
import database.Column;
import database.ProtoField;
import database.ProtoMetadata;
import database.SchemaBundle;
import database.SchemaBundleMetadata;
import database.SchemaOptions;
import database.Table;

import java.util.stream.Collectors;

public class PostgresSchemaGenerator extends SchemaGenerator {

    private static final String protoToPsqlType(Type type) {
        switch (type) {
            case INT32:
            case INT64:
                return "integer";
            case BOOL:
                return "boolean";
            default:
                return "varchar(100)";
        }
    }

    public static final String createDatabaseStatement(SchemaOptions options) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("CREATE DATABASE \"%s\";\n\n", options.getDatabaseName()));
        return sb.toString();
    }

    public static final String createTableStatement(Table table, SchemaBundleMetadata metadata) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("CREATE TABLE \"%s\" (\n", table.getName()));
        for (Column column : table.getColumnList()) {
            ProtoMetadata protoMetadata =
                    metadata.getProtoMetadataList().stream().filter(pm -> pm.getProtoImport().equals(column.getType()))
                            .findFirst().get();
            sb.append(String.format("    \"%s\" bytea,\n", column.getName()));
            for (ProtoField protoField : protoMetadata.getProtoFieldList()) {
                sb.append(String.format("    \"%s_%s\" %s NOT NULL,\n", column.getName(), protoField.getFieldName(),
                        protoToPsqlType(Descriptors.FieldDescriptor.Type.valueOf(protoField.getType()))));
            }

        }
        String pks = table.getPrimaryKeyList().stream().map(pk -> pk.getColumnName() + "_" + pk.getFieldName())
                .collect(Collectors.joining(", "));
        sb.append(String.format("    PRIMARY KEY (\"%s\")\n", pks));
        sb.append(");\n\n");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Flags.parse(args);
        new PostgresSchemaGenerator().handle();
    }

    @Override
    String generate(SchemaBundle bundle, SchemaOptions options) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(createDatabaseStatement(options));
        sb.append(String.format("\\connect \"%s\";\n\n", options.getDatabaseName()));
        for (Table table : bundle.getSchema().getTableList()) {
            sb.append(createTableStatement(table, bundle.getMetadata()));
        }
        return sb.toString();
    }
}
