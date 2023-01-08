package org.edward.blackbox.config;

public class SystemConfig {
    private static TcpServerConfig tcpServerConfig;
    private static ProtocolConfig protocolConfig;

    public static TcpServerConfig getTcpServerConfig() {
        return tcpServerConfig;
    }

    public static void setTcpServerConfig(TcpServerConfig config) {
        tcpServerConfig = config;
    }

    public static ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public static void setProtocolConfig(ProtocolConfig config) {
        protocolConfig = config;
    }
}