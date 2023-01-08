package org.edward.blackbox.runner;

import org.edward.blackbox.config.SystemConfig;
import org.edward.turbosnail.Papers;
import org.edward.turbosnail.Path;
import org.edward.turbosnail.ProtocolLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Order(1)
public class ProtocolServiceRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceRunner.class);

    private static Papers papers;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("loading protocol papers......");
        Map<String, Papers> papersMap = ProtocolLoader.load(new Path[]{new Path(SystemConfig
                .getProtocolConfig().getPath(), SystemConfig.getProtocolConfig().getId())});
        papers = papersMap.get(SystemConfig.getProtocolConfig().getId());
        logger.info("loading done");
    }

    public static Papers getPapers() {
        return papers;
    }
}