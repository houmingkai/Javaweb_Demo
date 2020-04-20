package com.hmk.javaweb.netty.chapter1.client;

import com.hmk.javaweb.netty.heart.IMIdleStateHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *  Netty客户端
 */
public class NettyClient {
    //重新连接次数
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args){
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        //数据交互
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }



    private static void connect(Bootstrap bootstrap,String host,Integer port,Integer retry){

        bootstrap.connect(host,port).addListener(future ->{
           if(future.isSuccess()){
               System.out.println("连接成功...");
           }else if(retry == 0){
               System.out.println("重连次数已经用完,放弃重连");
           }else{

               // 第几次重连
               int order = (MAX_RETRY - retry) + 1;
               // 本次重连的间隔
               //位移运算 '<<' 表示左移,左移一位都相当于乘以2的1次方，左移n位就相当于乘以2的n次方
               //1 << 3 表示1左移3位(1 * 2^3)
               int delay = 1 << order;
               System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
               //workerGroup 的 schedule 方法即可实现定时任务逻辑
               bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                       .SECONDS);

           }
        });
    }

}
