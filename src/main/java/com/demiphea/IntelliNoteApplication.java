package com.demiphea;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demiphea.dao")
public class IntelliNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelliNoteApplication.class, args);
    }

}
