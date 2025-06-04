package com.julianoclsantos.walletassignmentservice.infrastructure.mapper;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.IdentityHashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class CycleAvoidingMappingContext {
    private final Map<Object, Object> knownInstances = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getMappedInstance(Object source, Class<T> targetType) {
        return (T) knownInstances.get(source);
    }

    public void storeMappedInstance(Object source, Object target) {
        knownInstances.put(source, target);
    }
}
