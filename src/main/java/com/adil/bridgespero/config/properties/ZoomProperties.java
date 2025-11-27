package com.adil.bridgespero.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zoom")
public class ZoomProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String webhookSecret;
}
