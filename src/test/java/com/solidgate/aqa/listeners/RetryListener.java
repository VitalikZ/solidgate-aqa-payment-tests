package com.solidgate.aqa.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Slf4j
public class RetryListener implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        if (shouldRetry(testMethod, testClass)) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }

    private static boolean shouldRetry(Method method, Class<?> testClass) {
        if (method != null && method.isAnnotationPresent(RequiresRetry.class)) {
            return true;
        }
        if (testClass != null && testClass.isAnnotationPresent(RequiresRetry.class)) {
            return true;
        }
        if (method != null && method.getDeclaringClass().isAnnotationPresent(RequiresRetry.class)) {
            return true;
        }
        return false;
    }

    public static class RetryAnalyzer implements IRetryAnalyzer {
        private static final int MAX_RETRIES = 1;
        private int attempts = 0;

        @Override
        public boolean retry(ITestResult result) {
            if (attempts < MAX_RETRIES) {
                attempts++;
                log.warn("Retrying flaky test '{}' (attempt {}/{})",
                        result.getMethod().getMethodName(), attempts, MAX_RETRIES);
                return true;
            }
            return false;
        }
    }
}
