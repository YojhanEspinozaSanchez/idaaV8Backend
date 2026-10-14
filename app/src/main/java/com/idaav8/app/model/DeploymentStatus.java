package com.idaav8.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "deployment_status")
public class DeploymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name", nullable = false, length = 50)
    private String statusName;

    @OneToMany(mappedBy = "deploymentStatus")
    private List<Accelerator> accelerators = new ArrayList<>();

    public DeploymentStatus(){

    }

    public DeploymentStatus(String statusName){
        this.statusName = statusName;
    }

    public String getStatusName(){
        return statusName;
    }

    public void setStatusName(String statusName){
        this.statusName = statusName;
    }
    

}
