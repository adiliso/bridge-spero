package com.adil.bridgespero.domain.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstant {

    public static final DateTimeFormatter GROUP_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter RECORDING_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
}
