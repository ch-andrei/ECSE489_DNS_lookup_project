package com.app.dns_lookup;

import com.app.user_interface.TextUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsLookupRequest {

    private static final String DEFAULT_TIMEOUT = "500";
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
     * @return
     */
    public byte[] getServerNameAsByteArray(){
        byte bytes[] = new byte[4];
        List<String> items = Arrays.asList(getServerIp().split("\\."));
        int index = 0;
        for (String s : items){
            bytes[index++] = (byte)(int)Integer.valueOf(s);
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
