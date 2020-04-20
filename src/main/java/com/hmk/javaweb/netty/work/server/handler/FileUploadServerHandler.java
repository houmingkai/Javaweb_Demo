package com.hmk.javaweb.netty.work.server.handler;

import com.hmk.javaweb.netty.work.protocol.FilePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;


@ChannelHandler.Sharable
public class FileUploadServerHandler extends ChannelInboundHandlerAdapter {

    private int byteRead;
    private volatile Long start = 0L;
    private String file_dir = "D:\\netty_target";

    @Override   //当前channel从远端读取到数据时执行
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FilePacket) {
            FilePacket filePacket = (FilePacket) msg;
            byte[] bytes = filePacket.getBytes();
            byteRead = filePacket.getEndPos();
            String md5 = filePacket.getFile_md5();
            String path = file_dir + File.separator + md5;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
            } else {
                randomAccessFile.close();
            }
        }
    }

    @Override  //ChannelHandler回调方法出现异常时被回调
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
