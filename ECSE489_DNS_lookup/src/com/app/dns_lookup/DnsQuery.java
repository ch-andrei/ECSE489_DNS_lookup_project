package com.app.dns_lookup;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsQuery {

    private static final String DEFAULT_TIMEOUT = "5";
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
    public DnsQuery(){
        timeout = DEFAULT_TIMEOUT;
        maxRetries = DEFAULT_MAX_RETRIES;
        port = DEFAULT_PORT;
        requestType = DEFAULT_REQUEST_TYPE;
        serverIp = "";
        domainName = "";
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
