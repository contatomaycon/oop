package com.example.vehicle.application.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Cors cors) {
  public AppProperties {
    if (cors == null) {
      cors = new Cors(List.of("http://localhost:3000"));
    }
  }

  public record Cors(List<String> allowedOrigins) {
    public Cors {
      if (allowedOrigins == null || allowedOrigins.isEmpty()) {
        allowedOrigins = List.of("http://localhost:3000");
      }
    }
  }
}
