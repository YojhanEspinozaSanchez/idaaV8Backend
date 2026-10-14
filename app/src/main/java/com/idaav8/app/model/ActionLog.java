package com.idaav8.app.model;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "action_log")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_id", nullable = true)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "accel_id", nullable = false)
    private Accelerator accelerator;

    @Column(nullable = false)
    private LocalDateTime executionTimestamp;

   @ManyToOne
   @JoinColumn(name = "action_id", nullable = false) 
   private Action action;  




    // Constructors
    public ActionLog() {}

    //Getters & Setters
    public Employee getOwner(){
        return employee;
    }

    public void setOwner(Employee employee){
        this.employee = employee;
    }

    public Accelerator getAccelerator(){
        return accelerator;
    }

    public void setAccelerator(Accelerator accelerator){
        this.accelerator = accelerator;
    }

    public LocalDateTime getExecutionTimestamp(){
        return executionTimestamp;
    }

    public void setExecutionTimestamp(LocalDateTime executionTimestamp){
        this.executionTimestamp = executionTimestamp;

    }

    public Action getAction(){
        return action;
    }

    public void setAction(Action action){
        this.action = action;
    }





}
