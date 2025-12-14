package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Phone code is required")
    @Pattern(regexp = "^\\+\\d{1,4}$", message = "Phone code must start with + and contain 1–4 digits")
    private String phoneCode;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7–12 digits")
    private String phoneNumber;

    @NotEmpty(message = "At least one interest must be selected")
    private List<@NotBlank(message = "Interest cannot be empty") String> subjects;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
}