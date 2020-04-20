package com.hmk.javaweb.netty.work.client;

import com.hmk.javaweb.netty.work.client.handler.FileUploadClientHandler;
import com.hmk.javaweb.netty.work.protocol.FilePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

public class FileUploadClient {

    public static void main(String[] args){

        int port = 9090;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        FilePacket filePacket = new FilePacket();
        File file = new File("D:\\hmk\\book\\深入浅出MySQL全文.pdf");
        String fileMd5 = file.getName();
        filePacket.setFile(file);
        filePacket.setFile_md5(fileMd5);
        filePacket.setStartPos(0);     //要传输的文件的初始信息

        new FileUploadClient().connect("127.0.0.1", port, filePacket);
    }


    public void connect(String host, int port, final FilePacket filePacket) {
        EventLoopGroup group = new NioEventLoopGroup();  //只需要一个线程组，和服务端有所不同

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new ObjectEncoder());
                        channel.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                        channel.pipeline().addLast(new FileUploadClientHandler(filePacket));  //自定义的handler
                    }
                });
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(host, port).sync();   //使得链接保持
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
