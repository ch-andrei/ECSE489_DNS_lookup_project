package com.app.dns_lookup;

import com.app.user_interface.TextUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsLookupRequest {

    private static final String DEFAULT_TIMEOUT = "5000";
    private static final String DEFAULT_MAX_RETRIES = "3";
    private static final String DEFAULT_PORT = "53";
    private static final String DEFAULT_REQUEST_TYPE = "A";

    private String timeout;
    private String maxRetries;
    private String port;
    private String requestType;
    private String serverIp;
    private String domainName;

    /**
     *
     */
    public DnsLookupRequest(){
        timeout = DEFAULT_TIMEOUT;
        maxRetries = DEFAULT_MAX_RETRIES;
        port = DEFAULT_PORT;
        requestType = DEFAULT_REQUEST_TYPE;
        serverIp = "";
        domainName = "";
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public DnsLookupRequest(String[] args) throws IOException{
        this();
        initRequest(args);
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    private void initRequest(String[] args) throws IOException{
        String timeout = "";
        String maxRetries = "";
        String port = "";
        String requestType = "";
        String serverIp = "";
        String domainName = "";
        if (args.length < 2) {
            TextUI.printError(3, "DNS Client missing arguments!");
            return;
        }
        // parse arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-t")) {
                timeout += args[i+1];
            }
            else if (args[i].contains("-r")) {
                maxRetries += args[i+1];
            }
            else if (args[i].contains("-p")) {
                port += args[i+1];
            }
            else if(args[i].contains("-mx") || args[i].contains("-ns")){
                requestType += args[i].substring(1).toUpperCase();
            }
            //@ server name
            else if (args[i].startsWith("@")) {
                serverIp += args[i].substring(1);
                if (i == args.length - 1) {
                    TextUI.printError(2, "Domain name is missing!" );
                }
                else {
                    domainName += args[i + 1];
                }
            }
        }
        if (!timeout.equals(""))
            this.setTimeout(timeout);
        if (!maxRetries.equals(""))
            this.setMaxRetries(maxRetries);
        if (!port.equals(""))
            this.setPort(port);
        if (!requestType.equals(""))
            this.setRequestType(requestType);
        if (!serverIp.equals(""))
            this.setServerIp(serverIp);
        if (!domainName.equals(""))
            this.setDomainName(domainName);
    }

    /**
     *
     * @returnohhh
     */
    public byte[] getServerIpAsByteArray() throws NumberFormatException{
        byte bytes[] = new byte[4];
        List<String> items = Arrays.asList(getServerIp().split("\\."));
        int index = 0, val;
        for (String s : items){
            val = Integer.valueOf(s);
            if (val < 0 || val > 255)
                throw new NumberFormatException("");
            bytes[index++] = (byte)(int)val;
        }
        return bytes;
    }

    /**
     *
     * @return
     */
    public List<char[]> getDomainNameAsList(){
        ArrayList<char[]> labels = new ArrayList<>();
        int i = 0, j = 0, k = 0;
        while (i < getDomainName().length()){
            while (i < getDomainName().length() && getDomainName().charAt(i) != '.'){
                i++;
            }
            char[] label = new char[i-j];
            k = 0;
            while (j < i){
                label[k++] = getDomainName().charAt(j++);
            }
            labels.add(label);
            j++;
            i++;
        }
        return labels;
    }

    public String toString(){
        String out = "";
        out += "querying server " + getServerIp() + ", ";
        out += "for domain name [" + getDomainName() +"], ";
        out += "on port " + getPort() + ", ";
        out += getRequestType() + "-type request, ";
        out += "with " + getMaxRetries() +  " max retries, ";
        out += "and " + getTimeout() + " ms timeout delay.";
        return out;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(String maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
