package org.edward.blackbox.runner;

import org.edward.blackbox.config.SystemConfig;
import org.edward.blackbox.config.TcpServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class SystemConfigRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigRunner.class);

    private final TcpServerConfig tcpServerConfig;

    public SystemConfigRunner(TcpServerConfig tcpServerConfig) {
        this.tcpServerConfig = tcpServerConfig;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("loading system configurations......");
        SystemConfig.setTcpServerConfig(this.tcpServerConfig);
        logger.info("loading done");
    }
}