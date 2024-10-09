package org.example.registery;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommonResourceRegistry {
    Map<String, IResourceRegistry> registry;

    public CommonResourceRegistry() {
        registry = new ConcurrentHashMap<>();
    }

    public void register(String key, IResourceRegistry value) {
        registry.put(key, value);
    }

    public void unregister(String key) {
        registry.remove(key);
    }

    public Object get(String key) {
        return registry.get(key);
    }

}
