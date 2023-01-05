package org.edward.blackbox.config;

public class SystemConfig {
    private static TcpServerConfig tcpServerConfig;

    public static TcpServerConfig getTcpServerConfig() {
        return tcpServerConfig;
    }

    public static void setTcpServerConfig(TcpServerConfig config) {
        tcpServerConfig = config;
    }
}