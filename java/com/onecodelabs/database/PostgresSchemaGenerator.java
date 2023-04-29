package com.onecodelabs.database;

import com.onecodelabs.flags.Flags;
import database.Schema;

import java.io.IOException;

public class PostgresSchemaGenerator extends SchemaGenerator {
    @Override
    String generate(Schema schema) {
        return "hola mundo";
    }

    public static void main(String[] args) throws IOException {
        Flags.parse(args);
        new PostgresSchemaGenerator().handle();
    }
}
