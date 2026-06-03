package com.bank.teller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bank.teller", "com.bank.common"})
@MapperScan(basePackages = {"com.bank.teller.infrastructure.persistence", "com.bank.common"})
public class BankTellerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankTellerApplication.class, args);
    }
}
