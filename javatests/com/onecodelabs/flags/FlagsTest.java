package com.onecodelabs.flags;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class FlagsTest {

    @FlagSpec(name = "string_flag", description = "")
    public static Flag<String> stringFlag = Flag.empty();

    @FlagSpec(name = "string_list_flag", description = "")
    public static Flag<List<String>> stringListFlag = Flag.empty();

    @FlagSpec(name = "integer_flag", description = "")
    public static Flag<Integer> integerFlag = Flag.empty();

    @FlagSpec(name = "integer_list_flag", description = "")
    public static Flag<List<Integer>> integerListFlag = Flag.empty();

    @FlagSpec(name = "boolean_flag", description = "")
    public static Flag<Boolean> booleanFlag = Flag.of(false);

    @FlagSpec(name = "enum_flag", description = "")
    public static Flag<Color> enumFlag = Flag.empty();

    @FlagSpec(name = "enum_list_flag", description = "")
    public static Flag<List<Color>> enumListFlag = Flag.empty();

    @FlagSpec(name = "unset_flag", description = "")
    public static Flag<String> unsetFlag = Flag.empty();

    @FlagSpec(name = "unset_with_default_flag", description = "")
    public static Flag<String> unsetWithDefaultFlag = Flag.of("defaultValue");

    enum Color {
        RED, GREEN, BLUE
    }

    @BeforeClass
    public static void setup() {
        Flags.parseSystemProperties();
    }

    @Test
    public void stringFlag_success() {
        Assert.assertEquals(stringFlag.getNotNull(), "foo");
    }

    @Test
    public void stringListFlag_success() {
        Assert.assertEquals(stringListFlag.getNotNull().size(), 2);
        Assert.assertEquals(stringListFlag.getNotNull().get(0), "foo");
        Assert.assertEquals(stringListFlag.getNotNull().get(1), "bar");
    }

    @Test
    public void integerFlag_success() {
        Assert.assertEquals(integerFlag.getNotNull().intValue(), 4);
    }

    @Test
    public void integerListFlag_success() {
        Assert.assertEquals(integerListFlag.getNotNull().size(), 2);
        Assert.assertEquals(integerListFlag.getNotNull().get(0).intValue(), 1);
        Assert.assertEquals(integerListFlag.getNotNull().get(1).intValue(), 2);
    }

    @Test
    public void booleanFlag_success() {
        Assert.assertTrue(booleanFlag.getNotNull());
    }

    @Test
    public void enumFlag_success() {
        Assert.assertEquals(enumFlag.getNotNull(), Color.RED);
    }

    @Test
    public void enumListFlag_success() {
        Assert.assertEquals(enumListFlag.getNotNull().size(), 2);
        Assert.assertEquals(enumListFlag.getNotNull().get(0), Color.GREEN);
        Assert.assertEquals(enumListFlag.getNotNull().get(1), Color.BLUE);
    }

    @Test(expected = NullPointerException.class)
    public void unsetFlag_getNotNullFailure() {
        unsetFlag.getNotNull();
    }

    @Test
    public void unsetWithDefaultFlag_success() {
        Assert.assertEquals(unsetWithDefaultFlag.getNotNull(), "defaultValue");
    }
}
