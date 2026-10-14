package com.idaav8.app.dto;

import java.time.LocalDateTime;

public class AcceleratorDto {

    private long id;
    private String accelName;
    private String ipAddress;
    private String dns;
    private String description;
    private String state;
    private String deploymentStatus;
    private String owner;
    private LocalDateTime lastUpdate;

    public long getId(){
        return id;
    }

    public String getAccelName(){
        return accelName;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public String getDns(){
        return dns;
    }

    public String getDescription(){
        return description;
    }

    public String getState(){
        return state;
    }

    public String getDeploymentStatus(){
        return deploymentStatus;
    }

    public String getOwner(){
        return owner;
    }
    
    public LocalDateTime getLastUpdate(){
        return lastUpdate;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setAccelName(String accelName){
        this.accelName = accelName;
    }
    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }
    public void setDns(String dns){
        this.dns = dns;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setState(String state){
        this.state = state;
    }
    public void setDeploymentStatus(String deploymentStatus){
        this.deploymentStatus = deploymentStatus;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }
    public void setLastUpdate(LocalDateTime lastUpdate){
        this.lastUpdate = lastUpdate;
    }
}