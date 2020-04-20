package com.hmk.javaweb.netty.chapter1.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端写出数据");

        for (int i = 0; i < 1000; i++) {
            //获取数据
            ByteBuf byteBuf = getByteBuf(ctx);
            //写数据
            ctx.channel().writeAndFlush(byteBuf);
            
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){


        byte[] bytes ="你好，我是客户端!".getBytes(Charset.forName("utf-8"));
//        File file = new File("D:\\hmk\\book\\深入浅出MySQL全文.pdf");
//        byte[] bytes = fileConvertToByteArray(file);

        ByteBuf buffer = ctx.alloc().buffer();

        buffer.writeBytes(bytes);

        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

//        System.out.println(new Date() +  ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

    /**
     * 把一个文件转化为byte字节数组。
     * @return
     */
    private byte[] fileConvertToByteArray(File file) {
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
