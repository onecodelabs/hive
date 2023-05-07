package com.onecodelabs.serverframework.request;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ActionModule extends AbstractModule {

    private static final Pattern PATTERN_REQUEST_PATH = Pattern.compile("^/\\w+\\.\\w+$");

    private final ImmutableList<Class<? extends Action>> actionClasses;

    private ActionModule(ImmutableList<Class<? extends Action>> actionClasses) {
        this.actionClasses = actionClasses;
    }

    @SafeVarargs
    public static ActionModule forActions(Class<? extends Action>... actions) {
        return new ActionModule(ImmutableList.copyOf(Arrays.asList(actions)));
    }

    @Override
    protected void configure() {
        MapBinder<String, Action> mapbinder = MapBinder.newMapBinder(binder(), String.class, Action.class);
        for (Class<? extends Action> actionClass : actionClasses) {
            if (!actionClass.isAnnotationPresent(RequestPath.class)) {
                throw new IllegalStateException(
                        String.format("Class %s is missing @RequestPath annotation", actionClass.getName()));
            }
            RequestPath requestPath = actionClass.getAnnotation(RequestPath.class);
            if (requestPath.path() == null || requestPath.path().isEmpty()) {
                throw new IllegalStateException(
                        String.format("@RequestPath annotation in class %s is missing the \"path\" parameter.",
                                actionClass.getName()));
            }
            if (!PATTERN_REQUEST_PATH.matcher(requestPath.path()).matches()) {
                throw new IllegalStateException(
                        String.format("@RequestPath \"%s\" in class %s doesn't conform to pattern %s",
                                requestPath.path(), actionClass.getName(), PATTERN_REQUEST_PATH));
            }
            mapbinder.addBinding(requestPath.path()).to(actionClass);
        }
    }
}
