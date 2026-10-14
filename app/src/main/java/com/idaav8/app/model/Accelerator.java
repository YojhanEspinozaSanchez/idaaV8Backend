package com.idaav8.app.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "accel")
public class Accelerator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 255)
	private String accelName;
	
	@Column(nullable = false, length = 45)
	private String ipAddress;

	@Column(nullable = true, length = 100)
	private String dns;
	
	@Column(nullable = true, length = 300)
	private String description;
	
	@Column(nullable = true)
	private LocalDateTime lastUpdate;
	
	@ManyToOne
	@JoinColumn(name = "state_id", nullable = false)
	private State state;

	@ManyToOne
	@JoinColumn(name = "deployment_status_id", nullable = false)
	private DeploymentStatus deploymentStatus;
	
	@ManyToOne
	@JoinColumn(name = "emp_id", nullable = true)
	private Employee owner;

	@OneToMany(mappedBy = "accelerator")
	private List<ActionLog> actionLogs = new ArrayList<>();

	
	//Constructors
	public Accelerator() {}

	public Accelerator(String accelName, String ipAddress, String dns, String description, LocalDateTime lastUpdate, State state, DeploymentStatus deploymentStatus, Employee owner) {
		this.accelName = accelName;
		this.ipAddress = ipAddress;
		this.dns = dns;
		this.description = description;
		this.lastUpdate = lastUpdate;
		this.state = state;
		this.deploymentStatus = deploymentStatus;
		this.owner = owner;
	}

	


	//Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public DeploymentStatus getDeploymentStatus() {
		return deploymentStatus;
	}

	public void setDeploymentStatus(DeploymentStatus deploymentStatus) {
		this.deploymentStatus = deploymentStatus;
	}

	public String getDns(){
		return dns;
	}

	public void setDns(String dns){
		this.dns = dns;
	}

	public Employee getOwner() {
		return owner;
	}

	public void setOwner(Employee owner) {
		this.owner = owner;
	}
	
	

}
