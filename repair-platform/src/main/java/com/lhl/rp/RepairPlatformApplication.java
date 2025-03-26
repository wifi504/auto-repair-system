package com.lhl.rp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// 添加 Mapper 包扫描
@MapperScan("com.lhl.rp.repository")
@SpringBootApplication
// 启动定时任务
@EnableScheduling
public class RepairPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepairPlatformApplication.class, args);
    }

}
