package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.protocal.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        ctx.channel().writeAndFlush(login(msg));
    }

    private Object login(LoginRequestPacket msg) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(Packet.getVersion());
        if (valid(msg)){
            System.out.println("登录成功");
            responsePacket.setSuccess(true);
            responsePacket.setUserName(msg.getUserName());
        }else{
            System.out.println("登录失败");
            responsePacket.setSuccess(false);
        }
        return responsePacket;
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
