package com.wallet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication

public class Apptest {
	
    public static void main(String[] args) {
        SpringApplication.run(Apptest.class, args);
        myCustomRun();
    }

    public static void myCustomRun() {
        System.out.println("My custom run method");
        
    }
    
    
    
    
}
