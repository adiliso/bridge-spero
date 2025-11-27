package com.adil.bridgespero.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zoom")
public class ZoomOAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
