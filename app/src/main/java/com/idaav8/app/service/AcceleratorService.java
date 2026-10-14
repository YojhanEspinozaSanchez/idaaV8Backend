package com.idaav8.app.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idaav8.app.dto.AccelInfoAux;
import com.idaav8.app.dto.AcceleratorDto;
import com.idaav8.app.model.Accelerator;
import com.idaav8.app.model.Action;
import com.idaav8.app.model.ActionLog;
import com.idaav8.app.model.DeploymentStatus;
import com.idaav8.app.model.Employee;
import com.idaav8.app.model.State;
import com.idaav8.app.parser.AcceleratorParser;
import com.idaav8.app.repository.AcceleratorRepository;
import com.idaav8.app.repository.ActionLogRepository;
import com.idaav8.app.repository.ActionRespository;
import com.idaav8.app.repository.DeploymentStatusRepository;
import com.idaav8.app.repository.EmployeeRepository;
import com.idaav8.app.repository.StateRepository;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;


@Service
public class AcceleratorService {

    public final EmployeeRepository employeeRepository;
    public final StateRepository stateRepository;
    public final DeploymentStatusRepository deploymentRepository;
    public final AcceleratorRepository acceleratorRepository;
    public final ActionLogRepository actionLogRepository;
    public final ActionRespository actionRespository;
    private final SSHService sshService;

    public AcceleratorService(EmployeeRepository employeeRepository, StateRepository stateRepository, DeploymentStatusRepository deploymentRepository, AcceleratorRepository acceleratorRepository, ActionLogRepository actionLogRepository, SSHService sshService, ActionRespository actionRespository) {
        this.employeeRepository = employeeRepository;
        this.stateRepository = stateRepository;
        this.deploymentRepository = deploymentRepository;
        this.acceleratorRepository = acceleratorRepository;
        this.actionLogRepository = actionLogRepository;
        this.sshService = sshService;
        this.actionRespository = actionRespository;
    }

    

    
    //Get the username and password for SystemManager session
    @Value("${SYSMANAGER_USERNAME}")
    private String sysManagerUsername;

    @Value("${SYSMANAGER_PASSWORD}")
    private String sysManagerPassword;

    @Value("${ACCELPASSWORD}")
    private String accelPassword;



    public List<AccelInfoAux> getAccelsInfo() throws Exception {
        AcceleratorParser parser = new AcceleratorParser();
        try(SSHClient ssh = new SSHClient()) {

           
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect("idaui.dev.fyre.ibm.com", 22);
            ssh.authPassword(sysManagerUsername, sysManagerPassword);

            try(Session session = ssh.startSession()) {
                Session.Command cmd = session.exec("SystemManager idaasim list-simulators SIMULATOR-V8-S390X");
                String output = new String(cmd.getInputStream().readAllBytes());
                cmd.join(30, TimeUnit.SECONDS);
                return parser.parseAccelsInfo(output);
            }
        
        }
    
    }

    public List<Accelerator> createAccels() throws Exception{
        List<AccelInfoAux> accelsInfo = getAccelsInfo();
        List<Accelerator> accels = new ArrayList<>();

        long tempState = 3;
        

        
        try {
            for (AccelInfoAux accelInfo: accelsInfo){
                Accelerator accelerator = new Accelerator();
                
                String initialDescription = "Being used for testing";
                LocalDateTime lastUpdate = LocalDateTime.now(ZoneOffset.UTC);
                Optional<Employee> emp = employeeRepository.findByEmail(accelInfo.ownerEmail());
                
                State state = stateRepository
                                .findById(tempState)
                                .orElseThrow(() ->
                                new RuntimeException("State not found"));

                DeploymentStatus deployment = deploymentRepository
                                                .findByStatusName(accelInfo.deploymentStatus())
                                                .orElseThrow(() ->
                                                new RuntimeException("Deployment status not found"));

                if(emp.isPresent()){
                    accelerator.setOwner(emp.get());
                }
                

                accelerator.setAccelName(accelInfo.accelName());
                accelerator.setIpAddress(accelInfo.ipAddress());
                accelerator.setDns(accelInfo.dns());
                accelerator.setState(state);
                accelerator.setDescription(initialDescription);
                accelerator.setLastUpdate(lastUpdate);
                accelerator.setDeploymentStatus(deployment);

                acceleratorRepository.save(accelerator);
                accels.add(accelerator);
                

            }
            
        } catch (Exception e) {

        }
        
        return accels;
    }


    public boolean updateAccels() throws Exception {

        List<AccelInfoAux> accelsInfo = getAccelsInfo();
       

        String initialDescription =
                "Being used for testing";

        try {

            for (AccelInfoAux accelInfo : accelsInfo) {

                Optional<Accelerator> existingAccel =
                        acceleratorRepository.findByAccelName(
                                accelInfo.accelName());

                Accelerator accelerator =
                        existingAccel.orElse(
                                new Accelerator());

                boolean isNew =
                        existingAccel.isEmpty();

                boolean deployed =
                        accelInfo.deploymentStatus()
                                .equals("Deployed");

                long stateId = 3L;  // Unknown
                if(isAccelOnline(accelInfo.dns())){
                        stateId = 1L;

                }else{
                       stateId = 2L; 
                }

                LocalDateTime lastUpdate =
                        LocalDateTime.now(
                                ZoneOffset.UTC);

                Optional<Employee> emp =
                        employeeRepository.findByEmail(
                                accelInfo.ownerEmail());

                State state = stateRepository
                                .findById(stateId)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "State not found"));

                DeploymentStatus deployment =deploymentRepository
                                                .findByStatusName(
                                                        accelInfo.deploymentStatus())
                                                .orElseThrow(() ->
                                                        new RuntimeException(
                                                         "Deployment status not found"));

                String description;

                if (isNew) {

                    description =
                            deployed
                                    ? initialDescription
                                    : null;

                } else {

                    if (deployed) {

                        description =
                                accelerator.getDescription();

                        if (description == null) {

                            description =
                                    initialDescription;
                        }

                    } else {

                        description = null;
                    }
                }

                accelerator.setAccelName(
                        accelInfo.accelName());

                accelerator.setIpAddress(
                        accelInfo.ipAddress());

                accelerator.setDns(
                        accelInfo.dns());

                accelerator.setState(
                        state);

                accelerator.setDescription(
                        description);

                accelerator.setLastUpdate(
                        lastUpdate);

                accelerator.setDeploymentStatus(
                        deployment);

                accelerator.setOwner(
                        emp.orElse(null));

                acceleratorRepository.save(accelerator);

                
            }

        } catch (Exception e) {

                
            return false;
            
        }

        return true;
    }

    public List<AcceleratorDto> returnAccels() throws Exception{

        List<AcceleratorDto> accels = new ArrayList<>();

        try{

                for(Accelerator accelerator : acceleratorRepository.findAll()){
                        AcceleratorDto accel = new AcceleratorDto();


                        accel.setId(accelerator.getId());
                        accel.setAccelName(accelerator.getAccelName());
                        accel.setIpAddress(accelerator.getIpAddress());
                        accel.setDns(accelerator.getDns());
                        accel.setDescription(accelerator.getDescription());
                        accel.setState(accelerator.getState().getStateName());
                        accel.setDeploymentStatus(accelerator.getDeploymentStatus().getStatusName());
                        accel.setOwner(
                                accelerator.getOwner() != null
                                        ? accelerator.getOwner().getUsername()
                                        : null
                        );
                        accel.setLastUpdate(accelerator.getLastUpdate());
                        accels.add(accel);


                }

                

        } catch (Exception e) {

            throw e;
        }

        return accels;

    }
    
    public String getDwaStatus(String host)throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {

                Future<String> future =
                        executor.submit(() ->
                                sshService.executeCommand(
                                        host,
                                        "root",
                                        accelPassword,
                                        "podman exec dashDB bash -lc \"dwa-admin status\""
                                ));

                return future.get(
                        10,
                        TimeUnit.SECONDS);

        } catch (TimeoutException e) {

                throw new RuntimeException("Timeout getting DWA status");

        } finally {

                executor.shutdownNow();
        }
    }

    public String getVestaStatus(String host)throws Exception{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try{
                Future<String> future =
                        executor.submit(() ->
                        sshService.executeCommand(
                                host,
                                "root",
                                accelPassword,
                                "podman exec dashDB bash -lc \"/opt/ibm/dwa/bin/vesta status\""
                                
                        ));
                return future.get(
                        10,
                        TimeUnit.SECONDS);

        }catch(TimeoutException e){
                throw new RuntimeException("Timeout: getting Vesta status");
        } finally{
                executor.shutdown();
        }
    }

    public boolean isAccelOnline(String host) {

        try {

                String dwaOutput = getDwaStatus(host);
                String vestaStatus = getVestaStatus(host);

                return dwaOutput.contains(
                        "Active: active (running)"
                ) && isVestaHealthy(vestaStatus);

        } catch (Exception e) {

                System.err.println(
                        "Unable to get DWA status from "
                                + host
                                + ": "
                                + e.getMessage()
                );

                return false;
        }
    }

    public boolean isVestaHealthy(String vestaStatus) {

        return
                vestaStatus.contains("Vesta HA cronjob       : enabled")
                && vestaStatus.contains("cluster connectivity   : UP")
                && vestaStatus.contains("db2                    : UP")
                && vestaStatus.contains("idaa                   : UP")
                && vestaStatus.contains("in-sync                : UP");
    }

 

    public AcceleratorDto getAccelerator(long id){
        Optional<Accelerator> existingAccel =
                        acceleratorRepository.findById(id);

        Accelerator accelerator = existingAccel.orElse(null);

        AcceleratorDto accel = new AcceleratorDto();
                accel.setId(accelerator.getId());
                accel.setAccelName(accelerator.getAccelName());
                accel.setIpAddress(accelerator.getIpAddress());
                accel.setDns(accelerator.getDns());
                accel.setDescription(accelerator.getDescription());
                accel.setState(accelerator.getState().getStateName());
                accel.setDeploymentStatus(accelerator.getDeploymentStatus().getStatusName());
                accel.setOwner(
                        accelerator.getOwner() != null
                                ? accelerator.getOwner().getUsername()
                                : null
                );
                accel.setLastUpdate(accelerator.getLastUpdate());


        return accel;

    }
    


        public void restartDwa(Accelerator accelerator, Consumer<String> consumer) throws Exception {
                
                
                Optional<Action> action = actionRespository.findById(2L);
                
                ActionLog actionLog = new ActionLog();
                LocalDateTime executionTimestamp =
                        LocalDateTime.now(
                                ZoneOffset.UTC);
                actionLog.setOwner(accelerator.getOwner());
                actionLog.setAccelerator(accelerator);
                actionLog.setExecutionTimestamp(executionTimestamp);
                actionLog.setAction(action.orElse(null));
                actionLogRepository.save(actionLog);
                
        
                String host = accelerator.getDns();


                sshService.executeCommandStreaming(

                        host,

                        "root",

                        accelPassword,

                        "podman exec dashDB bash -lc \"dwa-admin restart && sleep 2 && dwa-admin status\"",

                        consumer
                );

                consumer.accept(
                        "Waiting for DWA to come back online..."
                );

                while (true) {

                        try {

                        String dwaStatus = getDwaStatus(host);
                      //  String vestaStatus = getVestaStatus(host);

                        if (dwaStatus.contains("Active: active (running)")) {
                                changeAcceleratorState(host);

                                consumer.accept(
                                        "=== RESTART_COMPLETED ==="
                                );



                                break;
                        }

                        } catch (Exception e) {
                        // ignore while the restart is in progress
                        }

                        Thread.sleep(5000);
                }


        }

        public void stopDwa(Accelerator accelerator, Consumer<String> consumer) throws Exception {

                Optional<Action> action = actionRespository.findById(3L);
                
                ActionLog actionLog = new ActionLog();
                LocalDateTime executionTimestamp =
                        LocalDateTime.now(
                                ZoneOffset.UTC);
                actionLog.setOwner(accelerator.getOwner());
                actionLog.setAccelerator(accelerator);
                actionLog.setExecutionTimestamp(executionTimestamp);
                actionLog.setAction(action.orElse(null));
                actionLogRepository.save(actionLog);
                String host = accelerator.getDns();

                sshService.executeCommandStreaming(

                        host,

                        "root",

                        accelPassword,

                        "podman exec dashDB bash -lc \"dwa-admin stop && sleep 2 && dwa-admin status\"",

                        consumer
                );

                consumer.accept(
                        "Waiting for DWA to stop..."
                );

                while (true) {

                        try {

                        String dwaStatus = getDwaStatus(host);
                      //  String vestaStatus = getVestaStatus(host);

                        if (dwaStatus.contains("Active: inactive (dead)")) {
                                changeAcceleratorState(host);
                                

                                consumer.accept(
                                        "=== STOP_COMPLETED ==="
                                );

                                break;
                        }

                        } catch (Exception e) {
                        // ignore while the stop is in progress
                        }

                        Thread.sleep(5000);
                }
        }

        public void startDwa(Accelerator accelerator, Consumer<String> consumer) throws Exception {

                Optional<Action> action = actionRespository.findById(1L);
                
                ActionLog actionLog = new ActionLog();
                LocalDateTime executionTimestamp =
                        LocalDateTime.now(
                                ZoneOffset.UTC);
                actionLog.setOwner(accelerator.getOwner());
                actionLog.setAccelerator(accelerator);
                actionLog.setExecutionTimestamp(executionTimestamp);
                actionLog.setAction(action.orElse(null));
                actionLogRepository.save(actionLog);
                String host = accelerator.getDns();

                sshService.executeCommand(
                        host,
                        "root",
                        accelPassword,
                        "podman start dashDB"
                );

                Thread.sleep(5000);

                sshService.executeCommandStreaming(

                        host,

                        "root",

                        accelPassword,

                         "podman exec dashDB bash -lc \"dwa-admin start && sleep 2 && dwa-admin status\"",

                        consumer
                );

                consumer.accept(
                        "Waiting for DWA to start..."
                );

                while (true) {

                        try {

                        String dwaStatus = getDwaStatus(host);
                      //  String vestaStatus = getVestaStatus(host);

                        if (dwaStatus.contains("Active: active (running)")) {
                                changeAcceleratorState(host);
                                

                                consumer.accept(
                                        "=== START_COMPLETED ==="
                                );

                                break;
                        }

                        } catch (Exception e) {
                        // ignore while the start is in progress
                        }

                        Thread.sleep(5000);
                }
                
        }

 


        void changeAcceleratorState(String host) throws Exception {
                Optional<Accelerator> accelerator = acceleratorRepository.findByIpAddress(host);
                State state = null;
                try{
                        if (accelerator.isPresent()){
                                if (isAccelOnline(host)){
                                        state = stateRepository
                                        .findById(1L)
                                        .orElseThrow(() ->
                                                new RuntimeException(
                                                        "State not found"));
                        }else{
                                state = stateRepository
                                .findById(2L)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "State not found"));
                        }

                                accelerator.get().setState(state);
                                acceleratorRepository.save(accelerator.get());
                        }
                }catch(Exception e){

                }
        
                
        }


        //////Update just one accelerator
        public boolean updateAccel(String accelName) throws Exception {

                List<AccelInfoAux> accelsInfo =
                        getAccelsInfo();

                AccelInfoAux accelInfo =
                        accelsInfo.stream()
                                .filter(a ->
                                        a.accelName()
                                                .equals(accelName))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Accelerator not found"));

                try {

                        Accelerator accelerator =
                                acceleratorRepository
                                        .findByAccelName(
                                                accelName)
                                        .orElseThrow(() ->
                                                new RuntimeException(
                                                        "Accelerator not found"));

                        boolean deployed =
                                accelInfo.deploymentStatus()
                                        .equals("Deployed");

                        long stateId =
                                isAccelOnline(
                                        accelInfo.dns())
                                                ? 1L
                                                : 2L;

                        LocalDateTime lastUpdate =
                                LocalDateTime.now(
                                        ZoneOffset.UTC);

                        Optional<Employee> emp =
                                employeeRepository.findByEmail(
                                        accelInfo.ownerEmail());

                        State state =
                                stateRepository
                                        .findById(stateId)
                                        .orElseThrow();

                        DeploymentStatus deployment =
                                deploymentRepository
                                        .findByStatusName(
                                                accelInfo.deploymentStatus())
                                        .orElseThrow();

                        String description =
                                deployed
                                        ? accelerator.getDescription()
                                        : null;

                        accelerator.setIpAddress(
                                accelInfo.ipAddress());

                        accelerator.setDns(
                                accelInfo.dns());

                        accelerator.setState(
                                state);

                        accelerator.setDescription(
                                description);

                        accelerator.setLastUpdate(
                                lastUpdate);

                        accelerator.setDeploymentStatus(
                                deployment);

                        accelerator.setOwner(
                                emp.orElse(null));

                        acceleratorRepository
                                .save(accelerator);

                        return true;

                } catch (Exception e) {

                        return false;
                }
        }

        public void resetAccelerator(Accelerator accelerator, Consumer<String> consumer)throws Exception {

                Optional<Action> action = actionRespository.findById(4L);
                
                ActionLog actionLog = new ActionLog();
                LocalDateTime executionTimestamp =
                        LocalDateTime.now(
                                ZoneOffset.UTC);
                actionLog.setOwner(accelerator.getOwner());
                actionLog.setAccelerator(accelerator);
                actionLog.setExecutionTimestamp(executionTimestamp);
                actionLog.setAction(action.orElse(null));
                actionLogRepository.save(actionLog);
                String name = accelerator.getAccelName();
                sshService.executeCommandStreaming(

                        "idaui.dev.fyre.ibm.com",

                        sysManagerUsername,

                        sysManagerPassword,

                        "SystemManager idaasim reset "
                                + getShortName(name) +
                                " SIMULATOR-V8-S390X latest",

                        consumer
                        
                );
                changeAcceleratorState(name);
        }

        public String getShortName(String name){

                return name.substring(
                        name.lastIndexOf("-") + 1
                );
        }

        public void updateDescription(Long id,String description)throws Exception {

                Accelerator accelerator =
                        acceleratorRepository
                                .findById(id)
                                .orElseThrow();

                accelerator.setDescription(
                        description
                );

                acceleratorRepository.save(
                        accelerator
                );
        }

       

}
