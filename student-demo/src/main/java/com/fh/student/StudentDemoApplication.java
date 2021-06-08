package com.fh.student;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fh.student.mapper")
public class StudentDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentDemoApplication.class, args);
    }

}
