package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(FrameDecoder.class);

    private final byte[] delimiter;
    private final int length;

    public FrameDecoder(byte[] delimiter, int length) {
        this.delimiter = delimiter;
        this.length = length;
    }

    private boolean findDelimiter = false;
    private int delimiterIndex = -1;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("FrameDecoder added");
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        if(this.length <= this.delimiter.length) {
            throw new Exception("the length of one frame must be greater than the length of delimiter");
        }
        if(byteBuf.readableBytes() < this.length) {
            return;
        }
        if(this.findDelimiter) {
            if(this.delimiterIndex > 0) {
                byteBuf.skipBytes(this.delimiterIndex-byteBuf.readerIndex());
                byteBuf.discardReadBytes();
                this.delimiterIndex = 0;
            }
            if(byteBuf.readableBytes() >= this.length) {
                list.add(byteBuf.readRetainedSlice(this.length));
                this.findDelimiter = false;
            }
        } else {
            this.delimiterIndex = ByteBufUtil.searchDelimiterIndex(byteBuf, this.delimiter);
            if(this.delimiterIndex >= 0) {
                this.findDelimiter = true;
                if(this.delimiterIndex > 0) {
                    byteBuf.skipBytes(this.delimiterIndex-byteBuf.readerIndex());
                    byteBuf.discardReadBytes();
                    this.delimiterIndex = 0;
                }
                if(byteBuf.readableBytes() >= this.length) {
                    list.add(byteBuf.readRetainedSlice(this.length));
                    this.findDelimiter = false;
                }
            } else {
                this.findDelimiter = false;
                byteBuf.skipBytes(byteBuf.readableBytes()-this.delimiter.length);
                byteBuf.discardReadBytes();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("FrameDecoder goes wrong", cause);
    }
}