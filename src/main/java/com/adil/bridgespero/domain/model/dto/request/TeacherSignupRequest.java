package com.adil.bridgespero.domain.model.dto.request;

import com.adil.bridgespero.domain.model.enums.Experience;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSignupRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone code is required")
    @Pattern(regexp = "^\\+\\d{1,4}$", message = "Phone code must start with + and contain 1–4 digits")
    private String phoneCode;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be 7–12 digits")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotEmpty(message = "At least one subject must be selected")
    private List<@NotBlank(message = "Subject cannot be empty") String> subjects;

    @NotNull(message = "Experience selection is required")
    private Experience experience;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @NotNull(message = "Demo video is required")
    private MultipartFile demoVideo;

    private boolean agreedToTerms;

    @Override
    public String toString() {
        return "TeacherSignupRequest{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", phoneCode='" + phoneCode + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", password='" + "******" + '\'' +
               ", confirmPassword='" + "******" + '\'' +
               ", subjects=" + subjects +
               ", experience=" + experience +
               ", bio='" + bio + '\'' +
               ", agreedToTerms=" + agreedToTerms +
               '}';
    }
}
