package com.hmk.javaweb.netty.file.work;

import com.hmk.javaweb.netty.file.handler.FileUploadClientHandler;
import com.hmk.javaweb.netty.file.util.FileUploadFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//文件上传客户端
public class FileUploadClient {
	public void connect(int port, String host,
			final FileUploadFile fileUploadFile) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
							ch.pipeline().addLast(new FileUploadClientHandler(fileUploadFile));
						}
					});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
			System.out.println("FileUploadClient connect()结束");
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		int port = FILE_PORT;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		List<String> fileList = new ArrayList<String>();
		fileList.add("D:\\hmk\\book\\深入浅出MySQL全文.pdf");
		fileList.add("D:\\hmk\\book\\编写高质量代码 改善Java程序的151个建议.pdf");
		try {
			for (int i = 0; i < fileList.size(); i++) {
				FileUploadFile uploadFile = new FileUploadFile();
				File file = new File(fileList.get(i));
				String fileMd5 = file.getName();// 文件名
				uploadFile.setFile(file);
				uploadFile.setFile_md5(fileMd5);
				uploadFile.setStarPos(0);// 文件开始位置
				new FileUploadClient().connect(port, "127.0.0.1", uploadFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int FILE_PORT = 9991;
}
