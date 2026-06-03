package com.bank.liability;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bank.liability", "com.bank.common"})
@MapperScan(basePackages = {"com.bank.liability.infrastructure.persistence", "com.bank.common"})
public class BankLiabilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankLiabilityApplication.class, args);
    }
}
