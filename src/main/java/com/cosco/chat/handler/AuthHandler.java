package com.cosco.chat.handler;

import com.cosco.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean hasLogin = SessionUtil.hasLogin(ctx.channel());
        System.out.println("AuthHandler检查登录状态: hasLogin=" + hasLogin);
        if (!hasLogin){
            System.out.println("未登录，关闭连接");
            ctx.channel().close();
        }else {
            System.out.println("已登录，移除AuthHandler");
            ctx.pipeline().remove(this);
            super.channelRead(ctx,msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (SessionUtil.hasLogin(ctx.channel())){
            System.out.println("当前连接登录验证完毕,无须再次验\n" +
                    "证, AuthHandler 被移除");
        }else{
            System.out.println("无登录验证，强制关闭连接!");
        }
    }
}
