package com.bank.liability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bank.liability", "com.bank.common"})
public class BankLiabilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankLiabilityApplication.class, args);
    }
}
