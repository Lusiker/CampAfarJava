package com.campfire.campafar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.campfire.campafar.Mapper")
public class CampAfarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampAfarApplication.class, args);
    }

}
