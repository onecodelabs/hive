package com.onecodelabs.database;

import com.google.protobuf.DescriptorProtos;
import com.onecodelabs.common.ParseUtils;
import com.onecodelabs.flags.Flags;
import database.ProtoField;
import database.ProtoMetadata;
import database.SchemaBundle;
import database.SchemaBundleMetadata;
import database.SchemaOptions;
import database.Table;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PostgresSchemaGenerator extends SchemaGenerator {

    private static final String protoToPsqlType(DescriptorProtos.FieldDescriptorProto.Type type) {
        switch (type) {
            case TYPE_INT32:
            case TYPE_INT64:
                return "integer";
            case TYPE_BOOL:
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
        for (String column : table.getColumnList()) {
            String columnName = ParseUtils.group(column, Constants.PATTERN_COLUMN, 1);
            String columnType = String.format("%s.%s", ParseUtils.group(column, Constants.PATTERN_COLUMN, 2),
                    ParseUtils.group(column, Constants.PATTERN_COLUMN, 3));
            ProtoMetadata protoMetadata =
                    metadata.getProtoMetadataList().stream().filter(pm -> pm.getProtoImport().equals(columnType))
                            .findFirst().get();
            sb.append(String.format("    \"%s\" bytea,\n", columnName));
            for (ProtoField protoField : protoMetadata.getProtoFieldList()) {
                sb.append(String.format("    \"%s_%s\" %s,\n", columnName, protoField.getFieldName(),
                        protoToPsqlType(DescriptorProtos.FieldDescriptorProto.Type.valueOf(protoField.getType()))));
            }

        }

        String pks = Arrays.stream(table.getPrimaryKeys().split(" ")).map(pk -> {
                    String pkColumnName = ParseUtils.group(pk, Constants.PATTERN_MESSAGE_ACCESS, 1);
                    String pkFieldAccesors = ParseUtils.group(pk, Constants.PATTERN_MESSAGE_ACCESS, 2);
                    // TODO: refactor once schema supports deep levels for primary keys
                    return pkColumnName + "_" + pkFieldAccesors;
                })
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
    String generate(SchemaBundle bundle, SchemaOptions options) {
        StringBuilder sb = new StringBuilder();
        sb.append(createDatabaseStatement(options));
        sb.append(String.format("\\connect \"%s\";\n\n", options.getDatabaseName()));
        for (Table table : bundle.getSchema().getTableList()) {
            sb.append(createTableStatement(table, bundle.getMetadata()));
        }
        return sb.toString();
    }
}
