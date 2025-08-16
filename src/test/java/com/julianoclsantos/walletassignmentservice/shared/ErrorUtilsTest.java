package com.julianoclsantos.walletassignmentservice.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorUtilsTest {

    @Test
    void shouldToFlatStackTraceWithSuccess() {
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException ex) {
            String result = ErrorUtils.toFlatStackTrace(ex, 5);

            assertNotNull(result);
            assertEquals(5, result.lines().count());
            assertTrue(result.contains("RuntimeException: Test exception"));
            assertTrue(result.contains("ErrorUtilsTest.shouldToFlatStackTraceWithSuccess"));
        }
    }

}