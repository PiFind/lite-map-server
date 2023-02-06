package io.pifind.mapserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAutoConfiguration
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class PoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoiApplication.class,args);
    }

}
