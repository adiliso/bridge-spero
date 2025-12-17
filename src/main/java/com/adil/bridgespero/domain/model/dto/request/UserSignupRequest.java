package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {

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

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @AssertTrue(message = "You must agree to the terms")
    private boolean agreedToTerms;

    @Override
    public String toString() {
        return "UserSignupRequest{" +
               "firstName='" + name + '\'' +
               ", lastName='" + surname + '\'' +
               ", email='" + email + '\'' +
               ", phoneCode='" + phoneCode + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", password='" + "******" + '\'' +
               ", confirmPassword='" + "******" + '\'' +
               ", bio='" + bio + '\'' +
               ", agreedToTerms=" + agreedToTerms +
               '}';
    }
}
