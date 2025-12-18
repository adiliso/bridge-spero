package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminCreateRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
        String password,

        @NotBlank(message = "Name is required")
        String Name,

        @NotBlank(message = "Surname is required")
        String Surname,

        @NotBlank(message = "Phone code is required")
        @Pattern(regexp = "^\\+\\d{1,4}$", message = "Phone code must start with + and contain 1–4 digits")
        String phoneCode,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7–12 digits")
        String phoneNumber
) {

    @Override
    public String toString() {
        return "AdminCreateRequest{" +
               "email='" + email + '\'' +
               ", password='" + "******" + '\'' +
               ", Name='" + Name + '\'' +
               ", Surname='" + Surname + '\'' +
               ", PhoneCode='" + phoneCode + '\'' +
               ", PhoneNumber='" + phoneNumber + '\'' +
               '}';
    }
}
