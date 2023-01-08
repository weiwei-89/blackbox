package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.edward.turbosnail.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputLogger extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(InputLogger.class);
    private static final String TAG_DEFAULT = "hex";

    private String tag;

    public InputLogger() {
        this.tag = TAG_DEFAULT;
    }

    public InputLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("InputLogger added");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof ByteBuf) {
                ByteBuf data = (ByteBuf) msg;
                logger.info("{}:{}", this.tag, DataUtil.toHexString(ByteBufUtil.getBytes(data)));
            }
        } finally {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("InputLogger goes wrong", cause);
    }
}