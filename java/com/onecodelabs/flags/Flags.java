package com.onecodelabs.flags;

import com.onecodelabs.flags.proto.Flag.FlagDescriptors;
import com.onecodelabs.flags.proto.Flag.FlagDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Flags {

    private static final Logger logger = Logger.getLogger(Flags.class.getName());
    private static boolean parsedSystemProperties = false;
    private static final Object LOCK = new Object();

    public static void parseSystemProperties() {
        synchronized (LOCK) {
            if (parsedSystemProperties) {
                return;
            }
            parsedSystemProperties = true;

            List<String> args = new ArrayList<>();
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                args.add(String.format("--%s=%s", entry.getKey(), entry.getValue()));
            }
            parse(args);
        }
    }

    public static void parse(List<String> argsList) {
        parse(argsList.toArray(new String[0]));
    }

    public static void parse(String[] args) {
        FlagDescriptors descriptors = loadDescriptors();
        Map<String, FlagDescriptor> map = descriptorMap(descriptors);
        Map<String, String> parsed = parseArgs(args);
        for (String key : parsed.keySet()) {
            FlagDescriptor descriptor = map.get(key);
            Field field = null;
            try {
                field = getAnnotatedField(descriptor);
            } catch(ClassNotFoundException|NoSuchFieldException|NullPointerException e) {
                logger.log(Level.FINEST, String.format("Failed to get annotated field for descriptor for flag: --%s\n", key));
                continue;
            }
            String intendedValue = parsed.get(key);
            try {
                handleField(descriptor, field, intendedValue);
            } catch(IllegalAccessException|ClassNotFoundException e) {
                logger.log(Level.WARNING, "Failed to set field value", e);
            }
        }
    }

    private static Class<?> findClassByName(String clazzName) throws ClassNotFoundException {
        String originalName = clazzName;
        String right = "";
        Matcher m;
        while ((m = Constants.CLASS_PATTERN.matcher(clazzName)).matches()) {
            try {
                Class clazz = Class.forName(String.format("%s.%s%s",m.group(1),m.group(2),right));
                return clazz;
            } catch (ClassNotFoundException e) {
                clazzName = m.group(1);
                right = "$" + m.group(2) + right;
            }
        }
        throw new ClassNotFoundException("Did not find class: " + originalName);
    }

    private static Object castSimpleFlagValue(String flagClass, String intendedValue) throws ClassNotFoundException {
        Class flagTypeClazz = findClassByName(flagClass);
        if (Enum.class.isAssignableFrom(flagTypeClazz)) {
            return Enum.valueOf(flagTypeClazz, intendedValue);
        }
        switch(flagClass) {
            case "java.lang.String":
                return intendedValue;
            case "java.lang.Integer":
                return Integer.valueOf(intendedValue);
            case "java.lang.Boolean":
                return Boolean.valueOf(intendedValue);
            default:
                throw new IllegalArgumentException("Unsupported flag type: " + flagClass);
        }
    }

    private static Object castFlagValue(String flagClass, String intendedValue) throws ClassNotFoundException {
        Class flagTypeClazz = findClassByName(flagClass);
        if (List.class.isAssignableFrom(flagTypeClazz)) {
            Matcher m = Constants.CLASS_PATTERN.matcher(flagClass);
            if (!m.matches()) {
                // TODO: handle
                throw new IllegalArgumentException("Unrecognized class pattern for List type: " + flagClass);
            }
            String listType = m.group(3);

            List list = new ArrayList();
            for (String s : intendedValue.split(",")) {
                list.add(castSimpleFlagValue(listType, s.trim()));
            }
            return list;
        }

        return castSimpleFlagValue(flagClass, intendedValue);
    }

    private static void handleField(FlagDescriptor descriptor, Field field, String intendedValue) throws IllegalAccessException, ClassNotFoundException {
        Object value = field.get(null);
        if (!(value instanceof Flag)) {
            logger.log(Level.WARNING, "Field for descriptor is not a flag: " + descriptor);
        } else {
            Flag flag = (Flag) value;
            flag.set(castFlagValue(descriptor.getFlagType(), intendedValue));
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            Matcher m;
            if ((m = Constants.BOOLEAN_FLAG_PATTERN.matcher(arg)).matches()) {
                map.put(m.group(1), "true");
            } else if ((m = Constants.VALUE_FLAG_PATTERN.matcher(arg)).matches()) {
                map.put(m.group(1), m.group(2));
            }

            i++;
        }
        return map;
    }

    private static Field getAnnotatedField(FlagDescriptor descriptor) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> clazz = Class.forName(descriptor.getClassName());
        Field f =  clazz.getDeclaredField(descriptor.getFieldName());
        f.setAccessible(true);
        return f;
    }

    private static Map<String, Field> getAnnotatedFields() {
        Map<String, Field> map = new HashMap<>();
        return map;
    }

    private static Map<String, FlagDescriptor> descriptorMap(FlagDescriptors descriptors) {
        return descriptors.getDescriptorsList().stream().collect(Collectors.toMap(FlagDescriptor::getName, Function.identity()));
    }

    private static FlagDescriptors loadDescriptors() {
        try {
            List<URL> urls = Collections.list(Flags.class.getClassLoader().getResources("flags.txt"));
            if (urls.size() == 0) {
                logger.log(Level.WARNING, "Flags file not found in urls: " + urls);
                return FlagDescriptors.getDefaultInstance();
            } else if (urls.size() > 1) {
                // more than 1 flags file
                throw new IllegalStateException("Found multiple flags files not found in urls: " + urls);
            } else {
                try (InputStream is = urls.get(0).openStream()) {
                    return FlagDescriptors.parseFrom(is);
                }
            }
        } catch(IOException e) {
            throw new IllegalStateException("Failed to load flags file", e);
        }
    }
}
