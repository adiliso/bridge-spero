package com.adil.bridgespero.domain.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ZoomCreateMeetingResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("join_url")
    String joinUrl;

    @JsonProperty("start_url")
    String startUrl;
}
