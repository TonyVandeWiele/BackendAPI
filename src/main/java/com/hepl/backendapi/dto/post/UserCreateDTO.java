package com.hepl.backendapi.dto.post;

import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Phone number must be valid")
    private String phone;

    @Valid
    @NotNull(message = "Address is required")
    private AddressCreateDTO address;

    @NotBlank(message = "Client account number is required")
    private String clientAccountNumber;

    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthday;

    @NotNull(message = "Marital status is required")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Sexe is required")
    private SexeEnum sexe;

    @NotNull(message = "Monthly salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly salary must be positive")
    @Digits(integer = 10, fraction = 2, message = "Salary must be a valid amount")
    private Double mensuelSalary;
}
