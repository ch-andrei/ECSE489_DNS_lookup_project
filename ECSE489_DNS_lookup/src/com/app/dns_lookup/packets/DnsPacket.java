package com.app.dns_lookup.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public abstract class DnsPacket {

    public static final int MAX_PACKET_SIZE = 1024; // in bytes
    public static final int HEADER_SIZE = 12; // in bytes

    protected ByteBuffer packetDataBuffer;
    protected DatagramPacket datagramPacket;

    public DnsPacket() {
        datagramPacket = null;
    }

    public ByteBuffer getPacketDataBuffer() {
        return packetDataBuffer;
    }

    public void setPacketDataBuffer(ByteBuffer packetDataBuffer) {
        this.packetDataBuffer = packetDataBuffer;
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }
}



