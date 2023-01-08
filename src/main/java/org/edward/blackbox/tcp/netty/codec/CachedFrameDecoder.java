package org.edward.blackbox.tcp.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CachedFrameDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CachedFrameDecoder.class);

    private final byte[] delimiter;
    private final int maxLength;

    public CachedFrameDecoder(byte[] delimiter, int maxLength) {
        this.delimiter = delimiter;
        this.maxLength = maxLength;
    }

    private boolean findDelimiter = false;
    private int delimiterIndex = -1;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("CachedFrameDecoder added");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(this.maxLength <= this.delimiter.length) {
            throw new Exception("the max length of one frame must be greater than the length of delimiter");
        }
        if(this.findDelimiter) {
            if(this.delimiterIndex > 0) {
                in.skipBytes(this.delimiterIndex-in.readerIndex());
                in.discardReadBytes();
                this.delimiterIndex = 0;
            }
            if(in.readableBytes() >= this.delimiter.length*2) {
                ByteBuf cache = in.slice(this.delimiter.length, in.readableBytes()-this.delimiter.length);
                int nextDelimiterIndex = ByteBufUtil.searchDelimiterIndex(cache, this.delimiter);
                if(nextDelimiterIndex >= 0) {
                    int middleBytesLength = this.delimiter.length + nextDelimiterIndex;
                    if(middleBytesLength >= this.maxLength) {
                        out.add(in.readRetainedSlice(this.maxLength));
                        this.findDelimiter = false;
                    } else {
                        out.add(in.readRetainedSlice(middleBytesLength));
                        this.findDelimiter = false;
                    }
                } else {
                    if(in.readableBytes() >= this.maxLength) {
                        out.add(in.readRetainedSlice(this.maxLength));
                        this.findDelimiter = false;
                    }
                }
            }
        } else {
            this.delimiterIndex = ByteBufUtil.searchDelimiterIndex(in, this.delimiter);
            if(this.delimiterIndex >= 0) {
                this.findDelimiter = true;
                if(this.delimiterIndex > 0) {
                    in.skipBytes(this.delimiterIndex-in.readerIndex());
                    in.discardReadBytes();
                    this.delimiterIndex = 0;
                }
                if(in.readableBytes() >= this.delimiter.length*2) {
                    ByteBuf cache = in.slice(this.delimiter.length, in.readableBytes()-this.delimiter.length);
                    int nextDelimiterIndex = ByteBufUtil.searchDelimiterIndex(cache, this.delimiter);
                    if(nextDelimiterIndex >= 0) {
                        int middleBytesLength = this.delimiter.length + nextDelimiterIndex;
                        if(middleBytesLength >= this.maxLength) {
                            out.add(in.readRetainedSlice(this.maxLength));
                            this.findDelimiter = false;
                        } else {
                            out.add(in.readRetainedSlice(middleBytesLength));
                            this.findDelimiter = false;
                        }
                    } else {
                        if(in.readableBytes() >= this.maxLength) {
                            out.add(in.readRetainedSlice(this.maxLength));
                            this.findDelimiter = false;
                        }
                    }
                }
            } else {
                this.findDelimiter = false;
                in.skipBytes(in.readableBytes()-this.delimiter.length);
                in.discardReadBytes();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("CachedFrameDecoder goes wrong", cause);
    }
}