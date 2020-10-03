package com.example.lenovo.myapp.Classes;


import java.util.HashMap;
import java.util.Map;

public class Wishlist {

    private String actId;
    private String orgId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Wishlist(){

    }
    public Wishlist(String actId, String orgId) {
        this.actId = actId;
        this.orgId = orgId;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}