package com.piotrwdowiak.junitTraining;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;

public class MockitoInitializer implements TestInstancePostProcessor {

    private static final Logger LOGGER = Logger.getLogger(MockitoInitializer.class.getName() );

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
        LOGGER.info("Initialized Mock for " + o.getClass());
        MockitoAnnotations.initMocks(o);
    }
}