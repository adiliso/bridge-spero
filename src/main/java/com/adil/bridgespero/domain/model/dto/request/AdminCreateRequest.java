package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
        String Surname
) {

        @Override
        public String toString() {
                return "AdminCreateRequest{" +
                       "email='" + email + '\'' +
                       ", password='" + "******" + '\'' +
                       ", Name='" + Name + '\'' +
                       ", Surname='" + Surname + '\'' +
                       '}';
        }
}
