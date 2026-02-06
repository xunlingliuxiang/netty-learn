package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.session.Session;
import com.cosco.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        LoginResponsePacket response = (LoginResponsePacket) login(msg);
        if (response.isSuccess()) {
            // 在服务端绑定 Session
            Session session = new Session(
                    msg.getUserId(),
                    msg.getUserName()
            );
            SessionUtil.bindSession(session, ctx.channel());
            System.out.println("用户id[ " + msg.getUserId() + "] " + "用户名[" + msg.getUserName() + "]已登录");
        }
        ctx.channel().writeAndFlush(response);
    }
    private Object login(LoginRequestPacket msg) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(Packet.getVersion());
        if (valid(msg)){
            System.out.println("登录成功");
            responsePacket.setSuccess(true);
            responsePacket.setUserName(msg.getUserName());
            responsePacket.setUserId(msg.getUserId());
            return responsePacket;
        }else{
            System.out.println("登录失败");
            responsePacket.setSuccess(false);
            return responsePacket;
        }
    }



    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
