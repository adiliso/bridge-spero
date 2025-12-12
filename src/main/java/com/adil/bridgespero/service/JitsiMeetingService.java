package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.MeetingResponse;
import com.adil.bridgespero.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@RequiredArgsConstructor
public class JitsiMeetingService implements MeetingService {

    @Value("${jitsi.base-url}")
    private String baseUrl;

    @Override
    public MeetingResponse createMeeting(String email, GroupEntity group) {
        String meetingUrl;
        if (group.getStartUrl() != null && !group.getStartUrl().isEmpty()) {
            meetingUrl = group.getStartUrl();
        } else {
            String safeGroupName = AppUtil.toSlug(group.getName());
            meetingUrl = baseUrl + "/" + safeGroupName + "-" + UUID.randomUUID();
        }
        return MeetingResponse.builder()
                .startUrl(meetingUrl)
                .joinUrl(meetingUrl)
                .build();
    }
}
