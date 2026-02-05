package com.cosco.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {
    public void start(Integer port) {
        // 1.创建两个NioEventLoopGroup boss用来监听端口,接受新的连接 worker用来处理数据读写
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 2.创建一个引导类ServerBootstrap ,引导服务的启动工作
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker) //配置两个nio线程组
                    .channel(NioServerSocketChannel.class) //指定服务端的IO模型为NIO
                    .childHandler(new ChannelInitializer<NioSocketChannel>() { //定义后续的逻辑处理
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new FirstServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);;
            // 绑定端口
            ChannelFuture sync = bind(serverBootstrap,port).sync();
//            ChannelFuture sync = serverBootstrap.bind(port)
//                    .addListener(future -> { //新增绑定后回调
//                        // 绑定失败后,递增端口直至绑定成功
//                        if (future.isSuccess()) {
//                            System.out.println("端口［" + port + "］绑定成功！");
//                        } else {
//                            System.err.println("端口［" + port + "］绑定失败！");
//                            bind(serverBootstrap, port + 1);
//                        }
//                    })
//                    .sync();
            sync.channel().closeFuture().sync();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            worker.shutdownGracefully();

            boss.shutdownGracefully();

        }
    }

    /**
     * 绑定失败后,递增端口直至绑定成功
     * @param serverBootstrap
     * @param port
     */
    private static ChannelFuture bind(final ServerBootstrap serverBootstrap, final int port) {
        ChannelFuture future = serverBootstrap.bind(port).awaitUninterruptibly();
        if (future.isSuccess()) {
            System.out.println("端口［" + port + "］绑定成功！");
            return future;
        } else {
            System.err.println("端口［" + port + "］绑定失败！");
            return bind(serverBootstrap, port + 1);
        }
    }


    public static void main(String[] args) {
        new NettyServer().start(8080);
    }

}
