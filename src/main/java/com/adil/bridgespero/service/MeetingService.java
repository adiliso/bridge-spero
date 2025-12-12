package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.MeetingResponse;

public interface MeetingService {

    MeetingResponse createMeeting(String email, GroupEntity group);
}
