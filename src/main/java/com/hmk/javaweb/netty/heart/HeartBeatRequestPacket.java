package com.hmk.javaweb.netty.heart;


import static com.hmk.javaweb.netty.heart.command.Command.HEARTBEAT_REQUEST;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
