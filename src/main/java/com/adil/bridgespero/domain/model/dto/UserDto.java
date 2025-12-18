package com.adil.bridgespero.domain.model.dto;

import com.adil.bridgespero.domain.entity.UserInterestEntity;
import com.adil.bridgespero.domain.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    @NotBlank
    private String email;

    @JsonIgnore
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    private Role role;

    @NotBlank
    private String phoneCode;

    @NotBlank
    private String phoneNumber;

    private Boolean enabled;

    private String bio;

    private Boolean agreedToTerms;

    private Instant createdAt;

    private Instant updatedAt;

    private Set<UserInterestEntity> userInterestEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

}


