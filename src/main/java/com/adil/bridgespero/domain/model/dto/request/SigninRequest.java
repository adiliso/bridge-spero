package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Override
    public String toString() {
        return String.format("SigninRequest{email='%s', password='******'}", email);
    }

}

