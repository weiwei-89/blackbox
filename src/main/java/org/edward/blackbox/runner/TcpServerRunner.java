package org.edward.blackbox.runner;

import org.edward.blackbox.config.SystemConfig;
import org.edward.blackbox.tcp.server.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TcpServerRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(TcpServerRunner.class);
    private static final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("tcp-server-"+count.getAndIncrement());
            return thread;
        }
    });

    @Override
    public void run(ApplicationArguments args) throws Exception {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("starting up tcp server@{}......", SystemConfig.getTcpServerConfig().getPort());
                    new TcpServer(SystemConfig.getTcpServerConfig()).startup();
                } catch(Exception e) {
                    logger.error("tcp server goes wrong", e);
                }
            }
        });
    }
}