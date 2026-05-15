package com.example.vehicle;

import com.example.vehicle.application.config.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class VehicleApplication {
  public static void main(String[] args) {
    SpringApplication.run(VehicleApplication.class, args);
  }
}

