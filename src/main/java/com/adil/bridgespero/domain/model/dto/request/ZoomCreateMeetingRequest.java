package com.adil.bridgespero.domain.model.dto.request;

public record ZoomCreateMeetingRequest(

        String topic,
        Integer type, // 1 = instant, 2 = scheduled
        Boolean join_before_host
) {
}
