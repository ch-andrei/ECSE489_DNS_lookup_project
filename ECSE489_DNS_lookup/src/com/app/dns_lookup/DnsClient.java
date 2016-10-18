package com.app.dns_lookup;

import com.app.dns_lookup.packets.DnsAnswerPacket;
import com.app.dns_lookup.packets.DnsPacket;
import com.app.dns_lookup.packets.DnsQuestionPacket;
import com.app.user_interface.TextUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.regex.Pattern;

public class DnsClient {

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void performDnsLookup(String[] args) throws IOException{
        DnsLookupRequest request;
        try {
            request = new DnsLookupRequest(args);
            if (validateDnsQuery(request)) {
                printRequest(request);
                performDnsLookup(request);
            } else
                TextUI.printError(3, "Invalid lookup format.");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param request
     * @throws IOException
     */
    public static void performDnsLookup(DnsLookupRequest request) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        int timeout = Integer.valueOf(request.getTimeout());
        socket.setSoTimeout(timeout);

        // loop sending packets until a response is received before timeout occurs or  {@maxRetries} number of packets sent
        DnsPacket questionPacket;
        DnsAnswerPacket answerPacket = null;
        int counter = 0;
        boolean receieved = false;
        long startTime = System.currentTimeMillis(), endTime = startTime, deltaTime;
        while (!receieved && counter < Integer.valueOf(request.getMaxRetries())) {
            // send question
            try {
                // setup packet
                questionPacket = new DnsQuestionPacket(request);
                TextUI.print("[" + (counter + 1) + "] Sending question...");
                socket.send(questionPacket.getDatagramPacket());
                TextUI.print("[" + (counter + 1) + "] Sending question complete.");
            } catch (IOException ie) {
                TextUI.printError(2, "Failed to send packet.");
                return;
            }

            // wait for response
            byte[] answerBuffer = new byte[DnsPacket.MAX_PACKET_SIZE];
            DatagramPacket answerDatagramPacket = new DatagramPacket(answerBuffer, answerBuffer.length);
            try {
                TextUI.print("[" + (counter + 1) + "] Waiting for response...");
                socket.receive(answerDatagramPacket);
                endTime = System.currentTimeMillis();
                answerPacket = new DnsAnswerPacket(answerDatagramPacket);
                TextUI.print("[" + (counter + 1) + "] Receieved response.");
                receieved = true;
            } catch (IOException ie) {
                TextUI.print("[" + (counter + 1) + "] Timeout: Delayed or no response from server.");
            }
            counter++;
        }
        if (receieved) {
            deltaTime = endTime - startTime;
            TextUI.printResponseTime("" + deltaTime, "" + (counter));
            TextUI.printAnswerSection("" + (answerPacket.parseAncount()));
            String out[] = answerPacket.parsePacketInfo();
            //for (String s : out){
            //    TextUI.print("\n" + s);
            //}
            if (out != null) {
                if (out[0].equals("A")) {
                    TextUI.printRecordsA(out[0], out[2], Integer.valueOf(out[1]), (answerPacket.parseAuthority()) ? "auth" : "nonauth");
                } else if (out[0].equals("NS") || out[0].equals("CNAME")) {
                    TextUI.printRecordsCNAMEorNS(out[0], out[2], Integer.valueOf(out[1]), (answerPacket.parseAuthority()) ? "auth" : "nonauth");
                } else if (out[0].equals("MX")) {
                    // TODO
                    TextUI.printRecrodsMX(out[0], out[2], out[2] ,Integer.valueOf(out[1]), (answerPacket.parseAuthority()) ? "auth" : "nonauth");
                } else {
                    // TODO
                    // print some error for unexpected type; shouldnt happen
                }
            }
            TextUI.printAdditionalSection("" + answerPacket.parseArcount());

        } else {
            TextUI.printError(4, "" + request.getMaxRetries());
        }
        // close the socket
        socket.close();
    }

    /**
     *
     * @param request
     */
    public static boolean validateDnsQuery(DnsLookupRequest request){
        // check if this is a valid request (if it doesnt contains any invalid entries)
        boolean valid = true;
        //IP Address Validation
        valid = valid && validateIP(request);
        //Options must be positive integer
        valid = valid && validateNumericOptions(request);
        return valid;
    }

    public static boolean validateIP(DnsLookupRequest request){
        if (!validateServerName(request))
            return false;
        try {
            request.getServerIpAsByteArray();
        } catch (NumberFormatException nfe){
            TextUI.printError(3, "IP value out of range.");
            return false;
        }
        return true;
    }

    public static boolean validateServerName(DnsLookupRequest request){
        if (request.getServerIp().equals("") || request.getDomainName().equals("")) {
            TextUI.printError(3, "There is no input for server or domain name.");
            return false;
        }
        return true;
    }

    public static boolean validateNumericOptions(DnsLookupRequest request){
        String numeric = "^\\d+$";
        if ((!Pattern.matches(numeric, request.getTimeout())) || (!Pattern.matches(numeric, request.getMaxRetries())) || (!Pattern.matches(numeric, request.getPort()))) {
            TextUI.printError(3, "Options [-t timeout] [-r max-retries] [-p port] fields can only take positive numeric values");
            return false;
        }
        return true;
    }

    public static void printRequest(DnsLookupRequest request){
        TextUI.printRequest(request.getDomainName(), request.getServerIp(), request.getRequestType());
    }
}