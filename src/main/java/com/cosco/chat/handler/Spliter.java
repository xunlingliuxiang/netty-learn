package com.cosco.chat.handler;

import com.cosco.chat.serialize.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class Spliter extends LengthFieldBasedFrameDecoder {
    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private boolean closed = false;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("decode被调用[" + this.hashCode() + "], 可读字节数: " + in.readableBytes() + ", closed=" + closed);
        
        if (closed) {
            System.out.println("连接已关闭，不再处理");
            return null;
        }
        
        // 屏蔽非本协议的客户端
        if (in.readableBytes() < 4) {
            System.out.println("数据不足4字节，无法检查魔数");
            return null;
        }
        
        if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
            System.out.println("接收到非本协议的信息:" + in.toString(CharsetUtil.UTF_8));
            closed = true;
            in.skipBytes(in.readableBytes());  // ← 添加这一行，消费掉所有数据
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
