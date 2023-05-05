package com.onecodelabs.database;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.TextFormat;
import com.onecodelabs.common.ParseUtils;
import com.onecodelabs.common.ProtoUtils;
import com.onecodelabs.flags.Flags;
import database.Schema;
import database.Table;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.onecodelabs.database.Constants.PATTERN_IMPORT;

public class SchemaConformanceTest {

    @BeforeClass
    public static void classSetup() {
        Flags.parseSystemProperties();
    }

    @Test
    public void databaseSchema_isValid() throws IOException {
        List<String> errors = new ArrayList<>();

        String fileContents = new String(Files.readAllBytes(Paths.get(DatabaseFlags.schemaPath.getNotNull())));
        Schema schema = TextFormat.parse(fileContents, Schema.class);

        Map<String, DescriptorProtos.FileDescriptorProto> descriptors =
                ProtoUtils.loadDescriptors(DatabaseFlags.protoLibs.getNotNull());

        Set<String> protoImports = new HashSet<>();
        Map<String, Table> tables = new HashMap<>();

        for (String protoImport : schema.getProtoImportList()) {
            String protoPackage;
            try {
                protoPackage = ParseUtils.group(protoImport, PATTERN_IMPORT, 1);
            } catch (Exception e) {
                errors.add(String.format("Proto import \"%s\" does not conform to %s", protoImport, PATTERN_IMPORT));
                continue;
            }
            if (!descriptors.containsKey(protoPackage)) {
                errors.add(String.format("Proto package \"%s\" is missing, only found %s", protoPackage,
                        descriptors.keySet()));
                continue;
            }
            protoImports.add(protoImport);
        }

        for (Table table : schema.getTableList()) {
            tables.put(table.getName(), table);
            Map<String, String> columns = new HashMap<>();
            for (String column : table.getColumnList()) {
                if (!Constants.PATTERN_COLUMN.matcher(column).matches()) {
                    errors.add(String.format("Column \"%s\" does not conform to pattern %s", column,
                            Constants.PATTERN_COLUMN));
                    continue;
                }
                String columnName = ParseUtils.group(column, Constants.PATTERN_COLUMN, 1);
                String columnType = String.format("%s.%s", ParseUtils.group(column, Constants.PATTERN_COLUMN, 2),
                        ParseUtils.group(column, Constants.PATTERN_COLUMN, 3));

                // type is declared as import
                if (!protoImports.contains(columnType)) {
                    errors.add(String.format("Column \"%s.%s\" references type with missing or failed import: %s",
                            table.getName(), columnName, columnType));
                    continue;
                }

                columns.put(columnName, column);
            }

            if (!Constants.PATTERN_PRIMARY_KEYS.matcher(table.getPrimaryKeys()).matches()) {
                errors.add(String.format("Primary keys string \"%s\" does not conform to pattern %s",
                        table.getPrimaryKeys(), Constants.PATTERN_PRIMARY_KEYS));
                continue;
            }

            for (String primaryKey : table.getPrimaryKeys().split(" ")) {
                String columnName = ParseUtils.group(primaryKey, Constants.PATTERN_MESSAGE_ACCESS, 1);
                String fieldAccesors = ParseUtils.group(primaryKey, Constants.PATTERN_MESSAGE_ACCESS, 2);
                String columnKey = table.getName() + "." + columnName;
                if (!columns.containsKey(columnName)) {
                    errors.add(String.format("Primary key references non-existent column: %s", columnKey));
                    continue;
                }
                String column = columns.get(columnName);
                String columnType = String.format("%s.%s", ParseUtils.group(column, Constants.PATTERN_COLUMN, 2),
                        ParseUtils.group(column, Constants.PATTERN_COLUMN, 3));

                if (!protoImports.contains(columnType)) {
                    errors.add(
                            String.format(
                                    "Primary key on column \"%s\" references type with missing or failed import: %s",
                                    columnKey, columnType));
                    continue;
                }

                String protoPackage = ParseUtils.group(columnType, PATTERN_IMPORT, 1);
                String protoMessage = ParseUtils.group(columnType, PATTERN_IMPORT, 2);
                DescriptorProtos.FileDescriptorProto fileDescriptorProto =
                        descriptors.get(protoPackage);
                Optional<DescriptorProtos.DescriptorProto> descriptorProto =
                        fileDescriptorProto.getMessageTypeList().stream()
                                .filter(message -> message.getName().equals(protoMessage))
                                .findFirst();

                // message exists
                if (!descriptorProto.isPresent()) {
                    errors.add(String.format("Message \"%s\" not found in package %s", protoMessage, protoPackage));
                    continue;
                }

                if (fieldAccesors.contains(".")) {
                    errors.add(String.format(
                            "Schema doesn't yet support deep field access in primary keys (found %d levels), only top-level fields can be used: \"%s\"",
                            fieldAccesors.split("\\.").length, fieldAccesors));
                    continue;
                }
                String fieldName = fieldAccesors;

                Optional<DescriptorProtos.FieldDescriptorProto> fieldDescriptorProto =
                        descriptorProto.get().getFieldList().stream()
                                .filter(field -> field.getName().equals(fieldName)).findFirst();

                // field exists
                if (!fieldDescriptorProto.isPresent()) {
                    errors.add(String.format("Field \"%s\" not found in message \"%s.%s\"", fieldName,
                            protoPackage, protoMessage));
                    continue;
                }

                // field is of primitive type
                if (Collections.singletonList(DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE).contains(
                        fieldDescriptorProto.get().getType())) {
                    errors.add(String.format("Field \"%s\" in Message \"%s.%s\" is not primitive", fieldName,
                            protoPackage, protoMessage));
                }
            }
        }

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Test failed with the following errors:\n");
        for (String error : errors) {
            errorMessage.append(" - " + error + "\n");
        }
        Assert.assertTrue(errorMessage.toString(), errors.isEmpty());
    }
}
