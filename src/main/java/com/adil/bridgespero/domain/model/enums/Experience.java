package com.adil.bridgespero.domain.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Experience {

    BEGINNER("0-1 il"),
    INTERMEDIATE("2-4 il"),
    EXPERIENCED("5-9 il"),
    EXPERT("10+ il");

    String description;
}
