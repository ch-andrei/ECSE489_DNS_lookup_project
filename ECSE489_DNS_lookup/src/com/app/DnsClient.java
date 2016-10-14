package com.app;

import java.util.ArrayList;
import java.io.IOException;

public class DnsClient {

    private static final String DEFAULT_TIMEOUT = "5";
    private static final String DEFAULT_MAX_RETRIES = "3";
    private static final String DEFAULT_PORT = "53";

    public static ArrayList<String> split_cmd_args(String[] args) throws IOException
    {
        String flag = "A";
        String server_ip = "";
        String domain_name = "";

        if (args.length < 3) {
            throw new IOException("DNS Client missing arguments!");
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                //TODO
            }
            //@ server name
            if (args[i].startsWith("@")) {
                server_ip = args[i].substring(1);
                if (i == args.length - 1) {
                    DnsAnswer.printError(2, "Domain name is missing!" );
                }
                else {
                    domain_name = args[i + 1];
                }
            }
        }

        if (server_ip.equals("") || domain_name.equals("")) {
            DnsAnswer.printError(2, "There is no input for server or domain name.");
        }

        ArrayList<String> command = new ArrayList<>();
        command.add(0, DEFAULT_TIMEOUT);
        command.add(1, DEFAULT_MAX_RETRIES);
        command.add(2, DEFAULT_PORT);
        command.add(3, flag);
        command.add(4, server_ip);
        command.add(5, domain_name);

        return command;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ArrayList lookup_args;
        try {
            lookup_args = split_cmd_args(args);
        } catch (Exception e){
            e.printStackTrace();
        }




    }
}