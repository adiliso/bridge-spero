package com.adil.bridgespero.domain.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("start_url")
    private String startUrl;

    @JsonProperty("join_url")
    private String joinUrl;

    @JsonProperty("password")
    private String password;
}