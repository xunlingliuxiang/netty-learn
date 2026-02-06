package com.cosco.chat.handler.server;

import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.session.Session;
import com.cosco.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        if (msg.isSuccess()){
            SessionUtil.bindSession(new Session(msg.getUserId(), msg.getUserName()), ctx.channel());
            System.out.println("客户端登录成功,用户:" + msg.getUserName());
        }
        else{
            System.out.println("客户端登录失败");
        }
    }
}
