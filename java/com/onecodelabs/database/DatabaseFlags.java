package com.onecodelabs.database;

import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;

public class DatabaseFlags {

    @FlagSpec(name = "file_path", description = "")
    public static final Flag<String> filePath = Flag.of("");
}
