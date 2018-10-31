package com.springmysql.financial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class FinancialApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialApplication.class, args);
    }
}
