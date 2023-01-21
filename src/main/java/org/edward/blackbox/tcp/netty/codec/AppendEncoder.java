package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppendEncoder extends MessageToByteEncoder<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(AppendEncoder.class);

    private final String content;

    public AppendEncoder(String content) {
        this.content = content;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("AppendEncoder added");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if(StringUtils.isBlank(this.content)) {
            out.writeBytes(msg);
            return;
        }
        out.writeBytes(msg.writeBytes(this.content.getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("AppendEncoder goes wrong", cause);
    }
}