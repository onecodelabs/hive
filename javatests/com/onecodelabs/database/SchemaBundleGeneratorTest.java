package com.onecodelabs.database;

import com.google.protobuf.TextFormat;
import com.onecodelabs.flags.Flag;
import com.onecodelabs.flags.FlagSpec;
import com.onecodelabs.flags.Flags;
import database.ProtoMetadata;
import database.SchemaBundle;
import database.SchemaBundleMetadata;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaBundleGeneratorTest {

    @FlagSpec(name = "schema_bundle", description = "")
    public static Flag<String> schemaBundlePath = Flag.empty();

    @FlagSpec(name = "bundle_metadata", description = "")
    public static Flag<String> bundleMetadataPath = Flag.empty();

    @BeforeClass
    public static void setup() {
        Flags.parseSystemProperties();
    }

    private static Optional<ProtoMetadata> find(SchemaBundleMetadata bundleMetadata, String protoImport) {
        return bundleMetadata.getProtoMetadataList().stream().filter(pm -> pm.getProtoImport().equals(protoImport))
                .findFirst();
    }

    @Test
    public void bundle_isValid() throws IOException {
        String schemaBundleContents = new String(Files.readAllBytes(Paths.get(schemaBundlePath.getNotNull())));
        String bundleMetadataFileContents = new String(Files.readAllBytes(Paths.get(bundleMetadataPath.getNotNull())));
        SchemaBundle schemaBundle = TextFormat.parse(schemaBundleContents, SchemaBundle.class);
        SchemaBundleMetadata controlMetadata = TextFormat.parse(bundleMetadataFileContents, SchemaBundleMetadata.class);
        SchemaBundleMetadata experimentMetadata = schemaBundle.getMetadata();

        Set<String> experimentImports = experimentMetadata.getProtoMetadataList().stream().map(
                ProtoMetadata::getProtoImport).collect(
                Collectors.toSet());
        Set<String> controlImports =
                controlMetadata.getProtoMetadataList().stream().map(ProtoMetadata::getProtoImport).collect(
                        Collectors.toSet());
        Assert.assertEquals("Control imports vs Experiment imports failed", controlImports, experimentImports);

        for (ProtoMetadata experimentProtoMetadata : experimentMetadata.getProtoMetadataList()) {
            ProtoMetadata controlProtoMetadata = find(controlMetadata, experimentProtoMetadata.getProtoImport()).get();
            Set<String> experimentFields = experimentProtoMetadata.getProtoFieldList().stream()
                    .map(pf -> pf.getFieldName() + "." + pf.getType()).collect(
                            Collectors.toSet());
            Set<String> controlFields = controlProtoMetadata.getProtoFieldList().stream()
                    .map(pf -> pf.getFieldName() + "." + pf.getType()).collect(
                            Collectors.toSet());

            Assert.assertEquals(String.format("Control fields vs Experiment fields for %s failed",
                    experimentProtoMetadata.getProtoImport()), controlFields, experimentFields);
        }
    }
}
