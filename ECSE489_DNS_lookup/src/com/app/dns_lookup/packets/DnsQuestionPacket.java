package com.app.dns_lookup.packets;

import com.app.dns_lookup.DnsQuery;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsQuestionPacket extends DnsPacket{

    static Random random;

    public DnsQuestionPacket(DnsQuery query) {
        this.random = new Random(System.currentTimeMillis());
        initPacket(query); // setup packetDataBuffer
        try {
            InetAddress lookupServer = InetAddress.getByAddress(query.getServerNameAsBytes());
            this.datagramPacket = new DatagramPacket(packetDataBuffer.array(), 0, packetDataBuffer.array().length, lookupServer, Integer.valueOf(query.getPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param query
     */
    private void initPacket(DnsQuery query) {
        initPacketHeader();
        initPacketData(query);
    }

    /**
     *
     */
    private void initPacketHeader() {
        // write id
        byte[] id = new byte[2];
        random.nextBytes(id);
        packetDataBuffer.put(id);
        // write QR|Opcode|AA|TC|RD|RA|Z|RCODE
        packetDataBuffer.put((byte)0x1);
        packetDataBuffer.put((byte)0x0);
        // write 1 to QDCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x1);
        // write 0 to ANCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
        // write 0 to NSCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
        // write 0 to ARCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
    }

    /**
     *
     * @param query
     */
    private void initPacketData(DnsQuery query) {
        // write QNAME
        List<char[]> labels = query.getDomainNameAsList();
        for(char[] label : labels){
            packetDataBuffer.put((byte)(label.length));
            for (char c : label){
                packetDataBuffer.put((byte)c);
            }
        }
        // write QTYPE (type-A, type-NS, type-MX)
        switch(query.getRequestType()){
            default: // write 0x0000: this should not happen
                packetDataBuffer.put((byte)0x0);
                packetDataBuffer.put((byte)0x0);
                break;
            case "A": // write 0x001
                packetDataBuffer.put((byte)0x0);
                packetDataBuffer.put((byte)0x1);
                break;
            case "NS": // write 0x0002
                packetDataBuffer.put((byte)0x0);
                packetDataBuffer.put((byte)0x2);
                break;
            case "MX": // write 0x000f
                packetDataBuffer.put((byte)0x0);
                packetDataBuffer.put((byte)0xf);
                break;
        }
        // write 0x0001 to QCLASS
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }
}



