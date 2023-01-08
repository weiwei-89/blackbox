package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.edward.turbosnail.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOLogger extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(IOLogger.class);
    private static final String INPUT_TAG_DEFAULT = "input_hex";
    private static final String OUTPUT_TAG_DEFAULT = "output_hex";

    private String inputTag;
    private String outputTag;

    public IOLogger() {
        this.inputTag = INPUT_TAG_DEFAULT;
        this.outputTag = OUTPUT_TAG_DEFAULT;
    }

    public IOLogger(String inputTag, String outputTag) {
        this.inputTag = inputTag;
        this.outputTag = outputTag;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("IOLogger added");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof ByteBuf) {
                ByteBuf data = (ByteBuf) msg;
                logger.info("{}:{}", this.inputTag, DataUtil.toHexString(ByteBufUtil.getBytes(data)));
            }
        } finally {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            if(msg instanceof ByteBuf) {
                ByteBuf data = (ByteBuf) msg;
                logger.info("{}:{}", this.outputTag, DataUtil.toHexString(ByteBufUtil.getBytes(data)));
            }
        } finally {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("IOLogger goes wrong", cause);
    }
}