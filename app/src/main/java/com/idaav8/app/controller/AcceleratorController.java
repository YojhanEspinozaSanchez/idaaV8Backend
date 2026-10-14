package com.idaav8.app.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.idaav8.app.dto.AcceleratorDto;
import com.idaav8.app.model.Accelerator;
import com.idaav8.app.repository.AcceleratorRepository;
import com.idaav8.app.service.AcceleratorService;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping ("/api/accelerator")
public class AcceleratorController {
    private final AcceleratorService acceleratorService;
    private final AcceleratorRepository accelRepository;
    

    public AcceleratorController(AcceleratorService acceleratorService, AcceleratorRepository accelRepository) {
        this.acceleratorService = acceleratorService;
        this.accelRepository = accelRepository;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Accelerator service is running.";
    }

    @GetMapping("/accelList")
    public List<AcceleratorDto> test() throws Exception {

        return acceleratorService.returnAccels();

    }

    @PostMapping("/updateAll")
    public ResponseEntity<Boolean>
    updateAccelerators() {

        try {

            return ResponseEntity.ok(
                    acceleratorService.updateAccels());

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(false);
        }
    }

   

    @GetMapping("/{id}/dwaStatus")
    public String getDwaStatus(@PathVariable Long id)throws Exception {

        Accelerator accelerator =
                accelRepository
                        .findById(id)
                        .orElseThrow();

        return acceleratorService
                .getDwaStatus(accelerator.getDns());
    }
    
    @GetMapping ("/{id}/vestaStatus")
    public String getVestaStatus(@PathVariable Long id)throws Exception{
        Accelerator accelerator =
                accelRepository
                        .findById(id)
                        .orElseThrow();
                        
        return acceleratorService
                .getVestaStatus(accelerator.getDns());
    }




    @GetMapping("/{id}/getAccelerator")
    public AcceleratorDto getAccelerator(@PathVariable Long id) {

        return acceleratorService.getAccelerator(id);
    }

    @GetMapping(
        value = "/{id}/restart",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)     
    public SseEmitter restartDwa(
            @PathVariable Long id) {

        SseEmitter emitter =
                new SseEmitter(0L);

        new Thread(() -> {

            try {

                Accelerator accelerator =
                        accelRepository
                                .findById(id)
                                .orElseThrow();

                acceleratorService.restartDwa(

                        accelerator,

                        line -> {

                            try {

                                emitter.send(line);

                            } catch (Exception e) {

                                throw new RuntimeException(e);
                            }
                        }
                );

                emitter.complete();

            } catch (Exception e) {

                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    @GetMapping(
        value = "/{id}/stop",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)     
    public SseEmitter stopDwa(
            @PathVariable Long id) {

        SseEmitter emitter =
                new SseEmitter(0L);

        new Thread(() -> {

            try {

                Accelerator accelerator =
                        accelRepository
                                .findById(id)
                                .orElseThrow();

                acceleratorService.stopDwa(

                        accelerator,

                        line -> {

                            try {

                                emitter.send(line);

                            } catch (Exception e) {

                                throw new RuntimeException(e);
                            }
                        }
                );

                emitter.complete();

            } catch (Exception e) {

                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    @GetMapping(
    value = "/{id}/start",
    produces = MediaType.TEXT_EVENT_STREAM_VALUE)     
    public SseEmitter startDwa(
            @PathVariable Long id) {

        SseEmitter emitter =
                new SseEmitter(0L);

        new Thread(() -> {

            try {

                Accelerator accelerator =
                        accelRepository
                                .findById(id)
                                .orElseThrow();

                acceleratorService.startDwa(

                        accelerator,

                        line -> {

                            try {

                                emitter.send(line);

                            } catch (Exception e) {

                                throw new RuntimeException(e);
                            }
                        }
                );

                emitter.complete();

            } catch (Exception e) {

                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }




    @PostMapping("/{id}/refresh")
    public boolean refreshAccelerator(
            @PathVariable Long id)
            throws Exception {

        Accelerator accelerator =accelRepository
                                            .findById(id)
                                            .orElseThrow();

        return acceleratorService
                .updateAccel(
                        accelerator.getAccelName());
    }


    @GetMapping(
    value = "/{id}/reset",
    produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter resetAccelerator(
            @PathVariable Long id) {

        SseEmitter emitter =
                new SseEmitter(0L);

        new Thread(() -> {

            try {

                Accelerator accelerator =
                        accelRepository
                                .findById(id)
                                .orElseThrow();

                acceleratorService
                        .resetAccelerator(

                                accelerator,

                                line -> {

                                    try {

                                        emitter.send(line);

                                    } catch (Exception e) {

                                        emitter.completeWithError(
                                                e);
                                    }
                                }
                        );

                emitter.complete();

            } catch (Exception e) {

                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<?> updateDescription(@PathVariable Long id,@RequestBody String description)throws Exception {

        acceleratorService
                .updateDescription(
                        id,
                        description
                );

        return ResponseEntity.ok().build();
    }

   





}
