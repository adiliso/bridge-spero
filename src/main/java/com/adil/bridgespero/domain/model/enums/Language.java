package com.adil.bridgespero.domain.model.enums;

import lombok.Getter;

@Getter
public enum Language {

    AZERBAIJANIAN("Azərbaycan dili"),
    ENGLISH("Ingilis dili"),
    RUSSIAN("Rus dili");

    private final String value;

    Language(String value) {
        this.value = value;
    }
}
