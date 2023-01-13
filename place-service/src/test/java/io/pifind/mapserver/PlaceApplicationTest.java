package io.pifind.mapserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class PlaceApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(PlaceApplicationTest.class,args);
    }

}
