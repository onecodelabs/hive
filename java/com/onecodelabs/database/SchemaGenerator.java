package com.onecodelabs.database;

import com.google.protobuf.TextFormat;
import com.onecodelabs.flags.Flags;
import database.Schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SchemaGenerator {
    abstract String generate(Schema schema);

    protected void handle() throws IOException {
        String fileContents = new String(Files.readAllBytes(Paths.get(DatabaseFlags.filePath.get())));
        Schema schema = TextFormat.parse(fileContents, Schema.class);

        // TODO: implement
    }
}
