package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.edward.blackbox.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputLogger extends ChannelOutboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(OutputLogger.class);
    private static final String TAG_DEFAULT = "hex";

    private String tag;

    public OutputLogger() {
        this.tag = TAG_DEFAULT;
    }

    public OutputLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("OutputLogger added");
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            if(msg instanceof ByteBuf) {
                ByteBuf data = (ByteBuf) msg;
                logger.info("{}:{}", this.tag, DataUtil.toHexString(ByteBufUtil.getBytes(data)));
            }
        } finally {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("OutputLogger goes wrong", cause);
    }
}