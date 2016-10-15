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
        initPacket(query); // setup packetData
        try {
            InetAddress lookupServer = InetAddress.getByAddress(query.getServerIp().getBytes());
            this.datagramPacket = new DatagramPacket(packetData.array(), 0, packetData.array().length, lookupServer, Integer.valueOf(query.getPort()));
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
        packetData.allocate(HEADER_SIZE);
        // write id
        byte[] id = new byte[2];
        random.nextBytes(id);
        packetData.put(id);
        // write QR|Opcode|AA|TC|RD|RA|Z|RCODE
        packetData.put((byte)0x1);
        packetData.put((byte)0x0);
        // write 1 to QDCOUNT
        packetData.put((byte)0x0);
        packetData.put((byte)0x1);
        // write 0 to ANCOUNT
        packetData.put((byte)0x0);
        packetData.put((byte)0x0);
        // write 0 to NSCOUNT
        packetData.put((byte)0x0);
        packetData.put((byte)0x0);
        // write 0 to ARCOUNT
        packetData.put((byte)0x0);
        packetData.put((byte)0x0);
    }

    /**
     *
     * @param domainName
     * @return
     */
    private ArrayList<char[]> parseDomainName(String domainName){
        ArrayList<char[]> labels = new ArrayList<>();
        int i = 0, j = 0, k = 0;
        while (i < domainName.length()){
            while (i < domainName.length() && domainName.charAt(i) != '.'){
                i++;
            }
            char[] label = new char[i-j];
            k = 0;
            while (j < i){
                label[k++] = domainName.charAt(j++);
            }
            labels.add(label);
            j++;
            i++;
        }
        return labels;
    }

    /**
     *
     * @param query
     */
    private void initPacketData(DnsQuery query) {
        // write QNAME
        List<char[]> labels = parseDomainName(query.getDomainName());
        for(char[] label : labels){
            packetData.put((byte)(label.length));
            for (char c : label){
                packetData.put((byte)c);
            }
        }
        // write QTYPE (type-A, type-NS, type-MX)
        switch(query.getRequestType()){
            default: // write 0x0000: this should not happen
                packetData.put((byte)0x0);
                packetData.put((byte)0x0);
                break;
            case "A": // write 0x001
                packetData.put((byte)0x0);
                packetData.put((byte)0x1);
                break;
            case "NS": // write 0x0002
                packetData.put((byte)0x0);
                packetData.put((byte)0x2);
                break;
            case "MX": // write 0x000f
                packetData.put((byte)0x0);
                packetData.put((byte)0xf);
                break;
        }
        // write 0x0001 to QCLASS
        packetData.put((byte)0x0);
        packetData.put((byte)0x0);
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }
}



