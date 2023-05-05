package com.onecodelabs.database;

import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;

import java.util.List;

public class DatabaseFlags {

    @FlagSpec(name = "schema_path", description = "")
    public static final Flag<String> schemaPath = Flag.empty();

    @FlagSpec(name = "input_bundle", description = "")
    public static final Flag<String> inputBundle = Flag.empty();

    @FlagSpec(name = "database_name", description = "")
    public static final Flag<String> databaseName = Flag.empty();

    @FlagSpec(name = "proto_libs", description = "")
    public static final Flag<List<String>> protoLibs = Flag.empty();
}
