package com.piotrwdowiak.junitTraining;

import org.junit.jupiter.api.extension.*;

import java.util.Date;
import java.sql.Timestamp;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

public class TimeoutExtension implements  BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static long timeStart;
    private static long timeStop;

    private static final long TIMEOUT = 100;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        Date date = new Date();
        timeStart = new Timestamp(date.getTime()).getTime();
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        if (shouldTestTimeout(extensionContext)) {
            Date date = new Date();
            timeStop = new Timestamp(date.getTime()).getTime();
            long executionTime = timeStop - timeStart;
            if (executionTime > TIMEOUT) {
                throw new IllegalStateException("Test should run no longer that " + TIMEOUT + " ms but took " +  executionTime + " ms");
            }
        }
    }

    private static boolean shouldTestTimeout(ExtensionContext context) {
        return context.getElement()
                .map(el -> isAnnotated(el, Timeout.class))
                .orElse(false);
    }
}