package com.app.dns_lookup.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public abstract class DnsPacket {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final int MAX_PACKET_SIZE = 512; // packet size limit, in bytes (as per RFC1035)
    public static final int HEADER_SIZE = 12; // in bytes
    public static final int MAX_LABEL_LENGTH = 64; // in bytes

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

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int i = 0; i < bytes.length; i++ ) {
            int index = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[index >>> 4];
            hexChars[i * 2 + 1] = hexArray[index & 0x0F];
        }
        return new String(hexChars);
    }
}



