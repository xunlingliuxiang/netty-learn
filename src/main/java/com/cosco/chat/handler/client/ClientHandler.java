package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import com.cosco.chat.serialize.PacketCodeC;
import com.cosco.chat.session.Session;
import com.cosco.chat.util.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端开始登录");
        LoginRequestPacket login = new LoginRequestPacket();
        login.setUserName("admin");
        login.setPassword("admin");
        login.setUserId(1);
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), login);
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet decode = PacketCodeC.INSTANCE.decode(byteBuf);
        if (decode instanceof LoginResponsePacket){
            LoginResponsePacket responsePacket = (LoginResponsePacket) decode;
            if (responsePacket.isSuccess()){
                SessionUtil.bindSession(new Session(responsePacket.getUserId(), responsePacket.getUserName()), ctx.channel());
                System.out.println("客户端登录成功,用户:" + responsePacket.getUserName());
            }
            else{
                System.out.println("客户端登录失败");
            }
        } else if (decode instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) decode;
            System.out.println(new Date() + "： 收到服务端的消息： " + messageResponsePacket.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("客户端发生异常: " + cause.getMessage());
        ctx.close();
    }
}
