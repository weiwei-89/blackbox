package org.edward.blackbox.tcp.netty.util;

import io.netty.buffer.ByteBuf;

public class ByteBufUtil {
    public static byte[] getBytes(ByteBuf data) throws Exception {
        ByteBuf part = data.slice();
        byte[] bytes = new byte[part.readableBytes()];
        part.readBytes(bytes);
        return bytes;
    }

    public static int searchDelimiterIndex(ByteBuf data, byte[] delimiter) throws Exception {
        if(data.readableBytes() < delimiter.length) {
            throw new Exception("the count of readable bytes must be greater than or equal to the length of delimiter");
        }
        boolean find = false;
        for(int b=data.readerIndex(); b<=data.readerIndex()+data.readableBytes()-delimiter.length; b++) {
            ByteBuf part = data.slice(b, delimiter.length);
            for(int d=0; d<delimiter.length; d++) {
                if(part.getByte(d) == delimiter[d]) {
                    find = true;
                } else {
                    find = false;
                    break;
                }
            }
            if(find) {
                return b;
            }
        }
        return -1;
    }
}