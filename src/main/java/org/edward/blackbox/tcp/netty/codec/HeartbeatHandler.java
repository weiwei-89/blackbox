package org.edward.blackbox.tcp.netty.codec;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HeartbeatHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
    private static final long DEFAULT_INTERVAL = 1000L;

    private final long interval;

    public HeartbeatHandler(long interval) {
        this.interval = interval;
    }

    private ScheduledFuture<?> schedule;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("HeartbeatHandler added");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.init(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.stop();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("HeartbeatHandler goes wrong", cause);
    }

    private void init(ChannelHandlerContext ctx) throws Exception {
        this.schedule = ctx.executor().schedule(new HeartbeatTask(ctx), 1L, TimeUnit.SECONDS);
    }

    private void stop() {
        if(this.schedule != null) {
            this.schedule.cancel(false);
            this.schedule = null;
        }
    }

    private abstract static class TimerTask implements Runnable {
        private final ChannelHandlerContext ctx;

        TimerTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                this.execute(this.ctx);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        protected abstract void execute(ChannelHandlerContext ctx) throws Exception;
    }

    private final class HeartbeatTask extends TimerTask {
        public HeartbeatTask(ChannelHandlerContext ctx) {
            super(ctx);
        }

        @Override
        protected void execute(ChannelHandlerContext ctx) throws Exception {
            logger.info("hahahahahahahahahaha");
            schedule = ctx.executor().schedule(this, 1L, TimeUnit.SECONDS);
        }
    }
}