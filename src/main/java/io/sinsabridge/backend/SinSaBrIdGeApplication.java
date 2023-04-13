package io.sinsabridge.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.sinsabridge.backend"})
public class SinSaBrIdGeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinSaBrIdGeApplication.class, args);
    }

}
