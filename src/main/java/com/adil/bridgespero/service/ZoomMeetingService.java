package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.request.MeetingRequest;
import com.adil.bridgespero.domain.model.dto.response.MeetingResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ZoomMeetingService implements MeetingService {

    RestTemplate restTemplate;
    ZoomOAuthService zoomOAuthService;

    public MeetingResponse createMeeting(String email, GroupEntity group) {
        String url = "https://api.zoom.us/v2/users/me/meetings";
        String topic = "Lesson: " + group.getName();

        MeetingRequest.MeetingSettings settings = MeetingRequest.MeetingSettings.builder()
                .joinBeforeHost(false)
                .participantVideo(true)
                .hostVideo(true)
                .muteUponEntry(true)
                .build();

        MeetingRequest requestBody = MeetingRequest.builder()
                .topic(topic)
                .type(1)
                .settings(settings)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(zoomOAuthService.getValidAccessToken(email));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MeetingRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<MeetingResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    MeetingResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Zoom meeting: " + e.getMessage());
        }
    }
}
