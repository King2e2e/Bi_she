package org.example.bi_she;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类。
 * 这里显式扫描 org.example 包，保证 controller、service、mapper 都能被加载到。
 */
@SpringBootApplication(scanBasePackages = "org.example")
@MapperScan("org.example.mapper")
public class BiSheApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiSheApplication.class, args);
        System.out.println("hello world");

    }

}
