package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record GroupMembersResponse(

        int count,
        List<UserCardResponse> students
) {
}
