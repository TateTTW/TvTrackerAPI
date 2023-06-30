package com.t8webs.tvtrackerapi.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TvTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TvTrackerApiApplication.class, args);
    }

}
