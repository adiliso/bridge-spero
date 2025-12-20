package com.adil.bridgespero.domain.model.dto.response;

public record JoinNotificationResponse(

        Long id,
        String studentName,
        String studentsSurname,
        String groupName
) {
}
