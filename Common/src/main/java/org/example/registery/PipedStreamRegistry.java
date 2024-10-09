package org.example.registery;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PipedStreamRegistry {
    private Map<String, StreamingPipe> registry;

    public PipedStreamRegistry() {
        registry = new ConcurrentHashMap<>();
    }

    public void register(String key, StreamingPipe pipe) {
        registry.put(key, pipe);
    }

    public void unregister(String key) {
        registry.remove(key);
    }

    public StreamingPipe get(String key) {
        return registry.get(key);
    }
}
