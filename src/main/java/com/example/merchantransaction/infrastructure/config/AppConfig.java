package com.example.merchantransaction.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.base-urls")
@Getter
@Setter
public class AppConfig {
    private String transactions;
    private String numerator;
}
