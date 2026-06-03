package com.bank.customer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bank.customer", "com.bank.common"})
@MapperScan(basePackages = {"com.bank.customer.infrastructure.persistence", "com.bank.common"})
public class BankCustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankCustomerApplication.class, args);
    }
}
