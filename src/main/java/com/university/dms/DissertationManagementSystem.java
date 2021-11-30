package com.university.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DissertationManagementSystem {

    private static ApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication.run(DissertationManagementSystem.class, args);
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }
}
