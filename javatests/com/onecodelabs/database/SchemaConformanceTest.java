package com.onecodelabs.database;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.onecodelabs.common.ProtoUtils;
import com.onecodelabs.common.ReflectionUtils;
import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;
import com.onecodelabs.flags.Flags;
import database.Column;
import database.PrimaryKey;
import database.Schema;
import database.Table;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

        Map<String, Message> defaultInstances = new HashMap<>();
        Map<String, Table> tables = new HashMap<>();
        Map<String, Column> columns = new HashMap<>();

        // generate default instances
        for (String protoImport : schema.getProtoImportList()) {
            Class<?> clazz;
            Method method;
            Message message;

            try {
                clazz = ReflectionUtils.findClassByName(protoImport);
            } catch (ClassNotFoundException e) {
                errors.add(String.format("Couldn't resolve proto import: %s", protoImport));
                continue;
            }
            try {
                method = clazz.getDeclaredMethod("getDefaultInstance");
            } catch (NoSuchMethodException e) {
                errors.add(String.format("Couldn't find getDefaultInstance() method in proto import: %s", protoImport));
                continue;
            }
            try {
                message = (Message) method.invoke(clazz);
            } catch (IllegalAccessException | InvocationTargetException e) {
                errors.add(
                        String.format("Couldn't invoke getDefaultInstance() method in proto import: %s", protoImport));
                continue;
            }
            defaultInstances.put(protoImport, message);
        }

        for (Table table : schema.getTableList()) {
            tables.put(table.getName(), table);
            for (Column column : table.getColumnList()) {
                columns.put(table.getName() + "." + column.getName(), column);

                // type is declared as import
                if (!defaultInstances.containsKey(column.getType())) {
                    errors.add(String.format("Column %s.%s references type with missing or failed import: %s",
                            table.getName(), column.getName(), column.getType()));
                    continue;
                }
            }

            for (PrimaryKey primaryKey : table.getPrimaryKeyList()) {
                String columnKey = table.getName() + "." + primaryKey.getColumnName();
                if (!columns.containsKey(columnKey)) {
                    errors.add(String.format("Primary key references non-existent column: %s", columnKey));
                    continue;
                }
                Column column = columns.get(columnKey);

                if (!defaultInstances.containsKey(column.getType())) {
                    errors.add(
                            String.format("Primary key on column %s references type with missing or failed import: %s",
                                    columnKey, column.getType()));
                    continue;
                }

                Message message = defaultInstances.get(column.getType());

                Optional<Descriptors.FieldDescriptor> descriptor =
                        ProtoUtils.getFieldDescriptor(message, primaryKey.getFieldName());

                // field exists
                if (!descriptor.isPresent()) {
                    errors.add(String.format("Field %s not found in Message %s", primaryKey.getFieldName(),
                            column.getType()));
                    continue;
                }

                // field is of primitive type
                if (!ProtoUtils.isPrimitiveType(descriptor.get().getType())) {
                    errors.add(String.format("Field %s in Message %s is not of primitive type, found %s",
                            primaryKey.getFieldName(), column.getType(), descriptor.get().getType()));
                    continue;
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
