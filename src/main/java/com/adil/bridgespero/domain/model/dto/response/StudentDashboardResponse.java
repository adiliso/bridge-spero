package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

@Builder
public class StudentDashboardResponse {


    String name;
    String surname;
    String profilePictureUrl;
    String bio;
    int activeGroups;
    double studyHoursThisWeek;
    int lessonsToday;

}
