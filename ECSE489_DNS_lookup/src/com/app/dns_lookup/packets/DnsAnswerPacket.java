package com.app.dns_lookup.packets;

import com.app.user_interface.TextUI;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrei on 2016-10-17.
 */
public class DnsAnswerPacket extends DnsPacket{

    private List<DnsAnswerSection> answers;

    public DnsAnswerPacket(DatagramPacket packet) {
        this.datagramPacket = packet;
        this.packetDataBuffer = ByteBuffer.wrap(packet.getData());
        this.answers = null;
    }

    public List<DnsAnswerSection> parseAndGetAnswers(){
        parsePacketInfo();
        return getAnswers();
    }

    private void parsePacketInfo(){
        // init array for output
        List<DnsAnswerSection> out = new ArrayList<>();

        byte rcode = parseRcode();
        switch (rcode){
            default:
                break;
            case 0:
                // no error condition
                break;
            case 1:
                TextUI.printError(2, "RCODE 1 Format error: the name server was unable to interpret the query");
                break;
            case 2:
                TextUI.printError(2, "RCODE 2 Server failure: the name server was unable to process this query due to some internal problem");
                break;
            case 3:
                TextUI.printError(1, "RCODE 3 Name error: domain name referenced in the query does not exist (meaningful only for responses from an authoritative name server) ");
                break;
            case 4:
                TextUI.printError(2, "RCODE 4 Not implemented: the name server does not support the requested kind of query");
                break;
            case 5:
                TextUI.printError(2, "RCODE 5 Refused: the name server refuses to perform the requested operation for policy reasons");
                break;
        }

        if (!parseRecursive()){
            TextUI.printError(2, "Contacted server does not allow recursion.");
        }

        int offset = HEADER_SIZE;
        // find end of QNAME
        while (packetDataBuffer.array()[offset] != 0x00 && offset < MAX_LABEL_LENGTH) {
            offset++;
        }
        offset++;

        // add 4 bytes for QTYPE and QCLASS
        offset += 4;

        //TextUI.print("start offset " + offset);

        int totalCount = parseAncount() + parseArcount() + parseAuthcount();
        int parsed = 0;
        while (parsed < totalCount) {
            // set up an answer
            DnsAnswerSection answer;
            if (parsed < parseAncount()){
                answer = new DnsAnswerSection(0); // answer section
            } else if( parsed < parseArcount()){
                answer = new DnsAnswerSection(1); // additional section
            } else {
                answer = new DnsAnswerSection(2); // authority section
            }

            //TextUI.print("[" + parsed + "]b4 name: " + offset);
            // find the end of NAME section
            if (checkReference(offset)){
                // if reference
                offset += 2;
            } else {
                // if actual name (structure similar to QNAME and RDATA
                while (packetDataBuffer.array()[offset] != 0x00 && offset < MAX_LABEL_LENGTH) {
                    offset++;
                }
                offset++;
            }
            //TextUI.print("[" + parsed + "]after name: " + offset);

            // get TYPE
            byte type = packetDataBuffer.array()[offset+1];
            if (type == 0x01)
                answer.setType("A");
            else if (type == 0x02)
                answer.setType("NS");
            else if (type == 0x0f)
                answer.setType("MX");
            else if (type == 0x05)
                answer.setType("CNAME");
            else {
                TextUI.printError(5, "Unexpected response TYPE {" + bytesToHex(new byte[]{type}) + "}");
            }

            // check CLASS
            offset += 2;
            byte pclass = packetDataBuffer.array()[offset+1];
            if (pclass != 0x01)
                TextUI.printError(5, "Unexpected response CLASS (not 0x0001): {" + bytesToHex(new byte[]{type}) + "}");

            // get TTL
            offset += 2;
            byte[] ttl = new byte[4];
            for (int i = 0; i < 4; i++) {
                ttl[i] = packetDataBuffer.array()[offset + i];
            }
            ByteBuffer temp = ByteBuffer.wrap(ttl);
            // need to get unsigned value
            answer.setTtl("" + ((long) temp.getInt(0) & 0xffffffffL));

            // get RDATA
            offset += 4;
            byte rdlength[] = new byte[]{packetDataBuffer.array()[offset], packetDataBuffer.array()[offset + 1]};
            temp = ByteBuffer.wrap(rdlength);
            int RDLENGTH = (temp.getShort() & 0xffff);
            //TextUI.print("rdlen" + RDLENGTH);

            offset += 2;
            if (answer.getType().equals("A")) {
                // get ip
                byte[] ip = new byte[4];
                for (int i = 0; i < 4; i++) {
                    ip[i] = packetDataBuffer.array()[offset++];
                }
                answer.setRdata(bytesIpToString(ip));
            } else if (answer.getType().equals("NS") || answer.getType().equals("CNAME")) {
                int[] offset_ptr = new int[]{offset};
                String str = recursiveParse(offset_ptr);
                str = str.substring(0, str.length()-1); // remove last .
                offset = offset_ptr[0];
                answer.setRdata(str);
            } else if (answer.getType().equals("MX")) {
                // get PREFERENCE field
                String str = "" + packetDataBuffer.getShort(offset);
                offset += 2;
                // get EXCHANGE field
                int[] offset_ptr = new int[]{offset};
                str += recursiveParse(offset_ptr);
                str = str.substring(0, str.length()-1); // remove last . (dot)
                offset = offset_ptr[0];
                answer.setRdata(str);
            } else {
                TextUI.printError(5, "Could not interpret answer packet.");
            }
            out.add(answer);
            parsed++;
        }

        this.answers = out;
    }

    /**
     * parses the current packet using
     * @param offset
     * @return
     */
    private String recursiveParse(int[] offset){
        //TextUI.print("**************\nrec parse starting offset " + offset[0]);
        String str = "";
        int label_offset = offset[0];
        byte b;
        if ((b = packetDataBuffer.array()[label_offset]) == 0) {
            //TextUI.print("REACHED THE END\n");
            offset[0] += 1;
            return str;
        } else if (checkReference(label_offset)){
            int temp_offset = (packetDataBuffer.getShort(label_offset) & 0x3fff);
            //TextUI.print("TEMPORARY OFFSET: " + temp_offset);
            int[] temp = new int[]{temp_offset};
            str += recursiveParse(temp);
            label_offset += 2;
            offset[0] = label_offset;
            return str;
        } else {
            int counter = b;
            //TextUI.print("name counter " + counter + "; offset " + label_offset);
            while (counter-- > 0) {
                b = packetDataBuffer.array()[++label_offset];
                str += (char)b;
            }
            str += ".";
            label_offset++;
            offset[0] = label_offset;
            return str + recursiveParse(offset);
        }
    }

    public byte[] parseID(){
        return (new byte[] {packetDataBuffer.array()[0], packetDataBuffer.array()[1]});
    }

    public boolean parseAuthority(){
        return (((packetDataBuffer.array()[2] & 0b00000100) == (0b00000100)) ? true : false);
    }

    public boolean parseRecursive(){
        return (((packetDataBuffer.array()[3] & 0b10000000) == (0b10000000)) ? true : false);
    }

    public byte parseRcode(){
        return (byte)(packetDataBuffer.array()[3] & 0b00001111);
    }

    public int parseAncount(){
        byte[] ancount = new byte[] {packetDataBuffer.array()[6], packetDataBuffer.array()[7]};
        ByteBuffer ancount_buf = ByteBuffer.wrap(ancount);
        return ancount_buf.getShort(0);
    }

    public int parseAuthcount(){
        byte[] ancount = new byte[] {packetDataBuffer.array()[8], packetDataBuffer.array()[9]};
        ByteBuffer ancount_buf = ByteBuffer.wrap(ancount);
        return ancount_buf.getShort(0);
    }

    public int parseArcount(){
        byte[] ancount = new byte[] {packetDataBuffer.array()[10], packetDataBuffer.array()[11]};
        ByteBuffer ancount_buf = ByteBuffer.wrap(ancount);
        return ancount_buf.getShort(0);
    }

    private boolean checkReference(int offset){
        byte nameStart = packetDataBuffer.array()[offset];
        //TextUI.print("ref check BYTE " + bytesToHex(new byte[] {nameStart}));
        if ((nameStart & (byte)0b11000000) == (byte)0b11000000) {
            //TextUI.print("found reference");
            return true;
        }
        return false;
    }

    public String bytesIpToString(byte[] bytes){
        String str = "";
        int i = 0;
        for (byte b : bytes) {
            str += ((b & 0b10000000) == 0b10000000) ? (b+256) : b;
            if (i++ != 3) str += ".";
        }
        return str;
    }

    public List<DnsAnswerSection> getAnswers() {
        return answers;
    }
}
