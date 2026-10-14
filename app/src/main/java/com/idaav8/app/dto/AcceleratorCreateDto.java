package com.idaav8.app.dto;

import java.time.LocalDateTime;

public class AcceleratorCreateDto {
 

	private String accelName;
	
	
	private String ipAddress;

	
	private String dns;
	
	
	
	private String description;
	
	
	private LocalDateTime lastUpdate;
	
	
	private String deploymentStatus;

    public AcceleratorCreateDto() {
    }


	public String getAccelName() {
		return accelName;
	}

	public void setAccelName(String accelName) {
		this.accelName = accelName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDeploymentStatus() {
		return deploymentStatus;
	}

	public void setDeploymentStatus(String deploymentStatus) {
		this.deploymentStatus = deploymentStatus;
	}

    public String getDns(){
        return dns;
    }

    public void setDns(String dns){
        this.dns = dns;
    }

    

}
