package be.genesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GenesisApplication {
    public static void main(String[] args) {
        SpringApplication.run(GenesisApplication.class, args);
    }
}
