package com.bank.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.bank.server",
    "com.bank.teller",
    "com.bank.customer",
    "com.bank.liability",
    "com.bank.common"
})
@MapperScan(basePackages = {
    "com.bank.teller.infrastructure.persistence",
    "com.bank.customer.infrastructure.persistence",
    "com.bank.liability.infrastructure.persistence"
})
public class BankServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankServerApplication.class, args);
    }
}
