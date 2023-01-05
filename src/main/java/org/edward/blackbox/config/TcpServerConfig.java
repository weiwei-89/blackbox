package org.edward.blackbox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TcpServerConfig {
    @Value("${tcp.server.port}")
    private int port;

    public int getPort() {
        return port;
    }
}