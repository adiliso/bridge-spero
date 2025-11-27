package com.adil.bridgespero.domain.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingRequest {

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("type")
    private int type;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("password")
    private String password;

    @JsonProperty("settings")
    private MeetingSettings settings;

    @Data
    @Builder
    public static class MeetingSettings {
        @JsonProperty("join_before_host")
        private boolean joinBeforeHost;

        @JsonProperty("participant_video")
        private boolean participantVideo;

        @JsonProperty("host_video")
        private boolean hostVideo;

        @JsonProperty("mute_upon_entry")
        private boolean muteUponEntry;
    }
}
