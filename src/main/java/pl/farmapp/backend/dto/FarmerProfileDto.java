package pl.farmapp.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FarmerProfileDto(

        @NotBlank(message = "Imię jest wymagane")
        String name,

        @NotBlank(message = "Nazwisko jest wymagane")
        String surname,

        @NotBlank(message = "Email jest wymagany")
        @Email(message = "Niepoprawny adres email")
        String email
) {}
