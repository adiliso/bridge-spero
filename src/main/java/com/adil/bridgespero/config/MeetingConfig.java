package com.adil.bridgespero.config;

import com.adil.bridgespero.service.JitsiMeetingService;
import com.adil.bridgespero.service.MeetingService;
import com.adil.bridgespero.service.ZoomMeetingService;
import com.adil.bridgespero.service.ZoomOAuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MeetingConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnProperty(
            name = "meeting.provider",
            havingValue = "jitsi",
            matchIfMissing = true
    )
    public MeetingService jitsiMeetingService() {
        return new JitsiMeetingService();
    }

    @Bean
    @ConditionalOnProperty(
            name = "meeting.provider",
            havingValue = "zoom"
    )
    public MeetingService zoomMeetingService(
            RestTemplate restTemplate,
            ZoomOAuthService zoomOAuthService
    ) {
        return new ZoomMeetingService(restTemplate, zoomOAuthService);
    }
}

