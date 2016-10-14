package com.app;

import java.io.IOException;

public class DnsClient {

    public static DnsQuery parseArgsForDnsQuery(String[] args) throws IOException
    {

        String timeout = "";
        String maxRetries = "";
        String port = "";
        String requestType = "";
        String serverIp = "";
        String domainName = "";

        DnsQuery query = new DnsQuery();

        if (args.length < 3) {
            throw new IOException("DNS Client missing arguments!");
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                //TODO
            }
            //@ server name
            if (args[i].startsWith("@")) {
                serverIp = args[i].substring(1);
                if (i == args.length - 1) {
                    TextUI.printError(2, "Domain name is missing!" );
                }
                else {
                    domainName = args[i + 1];
                }
            }
        }

        if (serverIp.equals("") || domainName.equals("")) {
            TextUI.printError(2, "There is no input for server or domain name.");
        }

        // TODO
        query.setTimeout(timeout);
        query.setMaxRetries(maxRetries);
        query.setPort(port);
        query.setRequestType(requestType);
        query.setServerIp(serverIp);
        query.setDomainName(domainName);

        return query;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DnsQuery query;
        try {
            query = parseArgsForDnsQuery(args);
        } catch (Exception e){
            e.printStackTrace();
        }




    }
}