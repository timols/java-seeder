package com.github.timols.seeder;

import com.google.inject.*;
import com.github.timols.seeder.annotation.Step;
import com.github.timols.seeder.step.SeedStep;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Seeder {

    private static final Logger logger = LoggerFactory.getLogger(Seeder.class);
    private static final Comparator<Class<? extends SeedStep>> STEPS_COMPARATOR = new Comparator<Class<? extends SeedStep>>() {
        @Override
        public int compare(Class<? extends SeedStep> o1, Class<? extends SeedStep> o2) {
            Integer first = Integer.parseInt(o1.getAnnotation(Step.class).number());
            Integer last = Integer.parseInt(o2.getAnnotation(Step.class).number());
            return first.compareTo(last);
        }
    };

    private final Reflections reflections;
    private final String stepPackageName;
    private final Injector injector;

    /**
     * Seeder constructor
     *
     * Decoupled from the Maven plugin Mojo so that the Seeder can be utilized
     * in a non-maven plugin context and to simplify testing.
     *
     * @param stepPackageName  The package that contains the SeedSteps
     */
    public Seeder(String stepPackageName) {
        this.stepPackageName = stepPackageName;
        this.reflections = new Reflections(stepPackageName);
        this.injector = createInjector();
    }

    public void seed() throws IllegalAccessException, InstantiationException {
        checkStepAnnotations();
        performSteps();
    }

    Injector createInjector() {
        return Guice.createInjector(Stage.DEVELOPMENT, getModules());
    }

    private void checkStepAnnotations() {
        for (Class<? extends SeedStep> seedStep : reflections.getSubTypesOf(SeedStep.class)) {
            Step stepAnnotation = seedStep.getAnnotation(Step.class);
            if (stepAnnotation == null) {
                throw new RuntimeException("You must specify a step and number for the class " + seedStep.getSimpleName() + " defined in the " + stepPackageName + "package");
            }
        }
    }

    private void performSteps() {
        for (Class<? extends SeedStep> aClass : getSteps()) {
            SeedStep instance = injector.getInstance(aClass);
            instance.perform();
        }
    }

    private SortedSet<Class<? extends SeedStep>> getSteps() {
        Set<Class<? extends SeedStep>> types = reflections.getSubTypesOf(SeedStep.class);
        SortedSet<Class<? extends SeedStep>> steps = new TreeSet<>(STEPS_COMPARATOR);
        steps.addAll(types);
        return steps;
    }

    private List<Module> getModules() {
        List<Module> instances = new ArrayList<>();
        Set<Class<? extends AbstractModule>> modules = reflections.getSubTypesOf(AbstractModule.class);
        for (Class<? extends Module> moduleClass : modules) {
            try {
                Module module = moduleClass.newInstance();
                instances.add(module);
            } catch (ReflectiveOperationException e) {
                logger.error(e.getCause().getMessage(), e);

            }
        }
        return instances;
    }
}
