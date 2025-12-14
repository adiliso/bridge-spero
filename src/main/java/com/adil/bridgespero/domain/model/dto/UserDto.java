package com.adil.bridgespero.domain.model.dto;

import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.domain.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

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

    private Role role;

    private UserStatus status;

    private String phone;

    private Boolean enabled;

    private String bio;

    private Boolean agreedToTerms;

    private Instant createdAt;

    private Instant updatedAt;

    private List<String> interests;

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


