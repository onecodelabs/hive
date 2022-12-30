package com.onecodelabs.flags;

import java.util.Arrays;
import java.util.List;
import com.onecodelabs.flags.proto.Flag.HelloEnum;

public class FlagsExample {

    @FlagSpec(name = "port", description = "port to use for server")
    private static Flag<Integer> port = Flag.of(8080);

    @FlagSpec(name = "dependency_address", description = "port to use for server")
    private static Flag<String> dependencyAddress = Flag.of("blade:foo");

    @FlagSpec(name = "my_value", description = "enum test")
    public static Flag<MyValue> myValue = Flag.of(MyValue.HOLA);

    @FlagSpec(name = "list_1", description = "some list")
    public static Flag<List<Integer>> list1 = Flag.of(Arrays.asList(1, 2, 3));

    @FlagSpec(name = "list_2", description = "some list 2")
    public static Flag<List<HelloEnum>> list2 = Flag.of(Arrays.asList(HelloEnum.HOLA, HelloEnum.CHAU));

    public enum MyValue {
        HOLA,MUNDO
    }

    public static void main(String[] args) {
        Flags.parse(args);
        System.out.println(port.get());
        System.out.println(dependencyAddress.get());
        System.out.println(myValue.get());
        System.out.println(list1.get());
        System.out.println(list2.get());
    }
}
