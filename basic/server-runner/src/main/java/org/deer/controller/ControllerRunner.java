package org.deer.controller;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import org.deer.controller.modules.InfraModule;

public class ControllerRunner {

    public static void main(String[] args) throws Exception {
        Guice.createInjector(ImmutableSet.of(
                new InfraModule()
        ));
        Thread.currentThread().join();
    }
}
