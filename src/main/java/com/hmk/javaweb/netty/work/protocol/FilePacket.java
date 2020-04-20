package com.hmk.javaweb.netty.work.protocol;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

import static com.hmk.javaweb.netty.work.protocol.Command.FILE;


@Data
public class FilePacket extends Packet implements Serializable {

    private File file;
    private String file_md5;
    private int startPos;
    private byte[] bytes;
    private int endPos;
    @Override
    public Byte getCommand() {
        return FILE;
    }
}
