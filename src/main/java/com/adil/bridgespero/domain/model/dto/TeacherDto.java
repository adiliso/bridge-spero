package com.adil.bridgespero.domain.model.dto;

import com.adil.bridgespero.domain.model.enums.Experience;
import com.adil.bridgespero.domain.model.enums.Role;
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
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto implements Serializable {

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

    private String phone;

    private Boolean enabled;

    private String bio;

    private Boolean agreedToTerms;

    private Instant createdAt;

    private Instant updatedAt;

    private List<String> interests;

    private Experience experience;

    private Double rating;

    private Integer ratingCount;

    private Set<String> subjects;

    private String demoVideoUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherDto teacherDto = (TeacherDto) o;
        return id.equals(teacherDto.id) && email.equals(teacherDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

}