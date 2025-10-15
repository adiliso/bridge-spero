package com.adil.bridgespero.domain.model.enums;

import lombok.Getter;

@Getter
public enum Experience {

    BEGINNER("0-1 il"),
    INTERMEDIATE("2-4 il"),
    EXPERIENCED("5-9 il"),
    EXPERT("10+ il");

    private final String description;

    Experience(String description) {
        this.description = description;
    }
}
