package com.app.dns_lookup.packets;

/**
 * Created by Andrei-ch on 2016-10-18.
 */
public class DnsAnswerSection {

    private int segmentType; // 0, 1, 2: answer, additional, authority
    private String name;
    private String type;
    private String aclass;
    private String ttl;
    private String rdata;

    public DnsAnswerSection(int segmentType) {
        name = "";
        type = "";
        aclass = "";
        ttl = "";
        rdata = "";
        this.segmentType = segmentType;
    }

    public int getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(int segmentType) {
        this.segmentType = segmentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAclass() {
        return aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getRdata() {
        return rdata;
    }

    public void setRdata(String rdata) {
        this.rdata = rdata;
    }
}
