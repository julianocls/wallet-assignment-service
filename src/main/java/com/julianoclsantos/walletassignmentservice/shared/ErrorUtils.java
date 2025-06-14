package com.julianoclsantos.walletassignmentservice.shared;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class ErrorUtils {

    public static String toFlatStackTrace(Throwable ex, int maxDepth) {
        if (ex == null) return "";

        return ex.getClass().getName() + ": " + ex.getMessage() + ", " +
                Arrays.stream(ex.getStackTrace())
                        .limit(maxDepth)
                        .map(stackTraceElement -> "at " +
                                stackTraceElement.getClassName() +
                                "." + stackTraceElement.getMethodName() +
                                "(" + stackTraceElement.getFileName() + ":" +
                                stackTraceElement.getLineNumber() + ")"
                        )
                        .collect(Collectors.joining(", "));
    }


}
