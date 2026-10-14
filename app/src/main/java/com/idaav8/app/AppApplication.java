package com.idaav8.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                AppApplication.class,
                args
        );
    }
}



/*
@SpringBootApplication
public class AppApplication implements CommandLineRunner {

    private final AcceleratorService accelService;

    public AppApplication(
            AcceleratorService accelService) {

        this.accelService = accelService;
    }

    public static void main(String[] args) {

        SpringApplication.run(
                AppApplication.class,
                args);
    }

    @Override
    public void run(String... args)
            throws Exception {

        accelService.restartDwa(

                "9.30.97.241",

                line ->
                        System.out.println(
                                "LOG: " + line)
        );
    }
}
*/


