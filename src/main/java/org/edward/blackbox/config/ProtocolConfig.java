package org.edward.blackbox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProtocolConfig {
    @Value("${protocol.path}")
    private String path;
    @Value("${protocol.id}")
    private String id;

    public String getPath() {
        return path;
    }
    public String getId() {
        return id;
    }
}