package org.example.input.micro;

import lombok.Getter;
import org.example.registery.CommonResourceRegistry;
import org.example.registery.PipedStreamRegistry;
import org.example.registery.StreamingPipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.example.CommonLabel.PipedReigstryLabel.LOCAL_MICRO;

@Component
@Getter
public class MicroRegistryService {

    private CommonResourceRegistry resourceRegistry;
    private PipedStreamRegistry pipedStreamRegistry;

    @Autowired
    public MicroRegistryService(CommonResourceRegistry registry, MicroRecorder microRecorder,
                                 PipedStreamRegistry pipedStreamRegistry) {
        this.resourceRegistry = registry;
        resourceRegistry.register(LOCAL_MICRO, microRecorder);
        this.pipedStreamRegistry = pipedStreamRegistry;
    }
}
