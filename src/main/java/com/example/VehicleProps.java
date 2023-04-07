package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vehicle")
public record VehicleProps(String apiUrl) {
}
