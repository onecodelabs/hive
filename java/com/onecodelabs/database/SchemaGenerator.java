package com.onecodelabs.database;

import com.google.protobuf.TextFormat;
import com.onecodelabs.common.AssertUtils;
import database.SchemaBundle;
import database.SchemaOptions;

import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SchemaGenerator {
    abstract String generate(SchemaBundle bundle, SchemaOptions options) throws Exception;

    protected void handle() throws Exception {
        AssertUtils.assumeTrue("Didn't find required flag --input_bundle", DatabaseFlags.inputBundle.get() != null);
        AssertUtils.assumeTrue("Didn't find required flag --database_name", DatabaseFlags.databaseName.get() != null);
        String inputBundle = DatabaseFlags.inputBundle.getNotNull();
        if (inputBundle.startsWith("//")) {
            inputBundle = inputBundle.substring(2).replace(":", "/") + ".bundle";
        }

        String fileContents = new String(Files.readAllBytes(Paths.get(inputBundle)));
        SchemaBundle bundle = TextFormat.parse(fileContents, SchemaBundle.class);
        SchemaOptions.Builder optionsBuilder = SchemaOptions.newBuilder();
        optionsBuilder.setDatabaseName(DatabaseFlags.databaseName.getNotNull());
        System.out.println(generate(bundle, optionsBuilder.build()).toString().trim());
    }
}
