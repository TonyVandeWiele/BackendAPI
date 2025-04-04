package com.hepl.backendapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateDTO {
    @NotNull(message = "Street cannot be empty")
    @NotBlank(message = "Street cannot be empty")
    @Size(max = 255, message = "Street name is too long")
    private String street;

    @NotNull(message = "City cannot be empty")
    @NotBlank(message = "City cannot be empty")
    @Size(max = 100, message = "City name is too long")
    private String city;

    @NotNull(message = "Number cannot be empty")
    @NotBlank(message = "Number cannot be empty")
    @Size(max = 20, message = "Number is too long")
    private String number;

    @NotNull(message = "Postal code cannot be empty")
    @NotBlank(message = "Postal code cannot be empty")
    @Size(max = 20, message = "Postal code is too long")
    private String zipCode;

    @NotNull(message = "Country cannot be empty")
    @NotBlank(message = "Country cannot be empty")
    @Size(max = 100, message = "Country name is too long")
    private String country;
}
