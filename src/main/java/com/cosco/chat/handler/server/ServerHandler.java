package com.cosco.chat.handler.server;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import com.cosco.chat.serialize.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            Packet decode = PacketCodeC.INSTANCE.decode(byteBuf);

            if (decode == null) {
                System.out.println("解码失败,收到未知数据包");
                return;
            }

            LoginResponsePacket responsePacket = new LoginResponsePacket();
            responsePacket.setVersion(Packet.getVersion());

            if (decode instanceof LoginRequestPacket){
                LoginRequestPacket loginRequestPacket = (LoginRequestPacket) decode;
                if (valid(loginRequestPacket)){
                    System.out.println("登录成功");
                    responsePacket.setSuccess(true);
                    responsePacket.setUserName(loginRequestPacket.getUserName());
                }else{
                    System.out.println("登录失败");
                    responsePacket.setSuccess(false);
                }
                ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), responsePacket);
                ctx.channel().writeAndFlush(encode);
            } else if (decode instanceof MessageRequestPacket) {
                MessageRequestPacket messageRequestPacket = (MessageRequestPacket) decode;
                System.out.println(new Date() + ":服务端收到消息:" + messageRequestPacket.getMessage());
                MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
                messageResponsePacket.setMessage("服务端回复【" +
                        messageRequestPacket.getMessage() + "】");
                ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(),
                        messageResponsePacket);
                ctx.channel().writeAndFlush(responseByteBuf);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("服务端发生异常: " + cause.getMessage());
        ctx.close();
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
