package com.adil.bridgespero.domain.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Language {

    AZERBAIJANIAN("Azərbaycan dili"),
    ENGLISH("Ingilis dili"),
    RUSSIAN("Rus dili");

    String value;
}
