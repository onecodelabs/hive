package com.onecodelabs.flags;

import com.google.auto.service.AutoService;
import com.onecodelabs.flags.proto.Flag.FlagDescriptor;
import com.onecodelabs.flags.proto.Flag.FlagDescriptors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SupportedAnnotationTypes("com.onecodelabs.flags.FlagSpec")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FlagsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        FlagDescriptors.Builder descriptors = FlagDescriptors.newBuilder();
        for (Element element : roundEnv.getElementsAnnotatedWith(FlagSpec.class)) {
            FlagSpec flagSpec = element.getAnnotation(FlagSpec.class);

            Matcher matcher = Constants.CLASS_PATTERN.matcher(element.asType().toString());
            if (!matcher.matches()) {
                // TODO: handle
            }

            FlagDescriptor descriptor = FlagDescriptor.newBuilder()
                    .setName(flagSpec.name())
                    .setClassName(element.getEnclosingElement().toString())
                    .setFieldName(element.toString())
                    .setFlagType(matcher.group(3))
                    .build();
            descriptors.addDescriptors(descriptor);
        }

        if (descriptors.getDescriptorsCount() == 0) {
            return false;
        }

        try(OutputStream os = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "flags.txt").openOutputStream()) {
//            String s = descriptors.build().toString();
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Content size: " + s.length());
//            writer.write(s);
            descriptors.build().writeTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        for (TypeElement annotation : annotations) {
//            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
//            for (Element annotatedElement : annotatedElements) {
//                FlagSpec flagSpec = annotatedElement.getAnnotation(FlagSpec.class);
//                flagSpecs.put(flagSpec.name(), annotatedElement);
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("Found flag %s, kind %s", flagSpec.name(), annotatedElement.getKind()));
//                VariableElement variableElement = (VariableElement) annotatedElement;
//
//            }
//        }
        return true;
    }
}
