package com.hepl.backendapi.dto.post;

import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.RoleEnum;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {

    @Size(max = 150, message = "Email must be at most 150 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Phone number must be valid")
    private String phone;

    @Valid
    private AddressCreateDTO address;

    @NotBlank(message = "Client account number is required")
    private String clientAccountNumber;

    @Past(message = "Birthdate must be in the past")
    private LocalDate birthday;

    private MaritalStatus maritalStatus;

    private SexeEnum sexe;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly salary must be positive")
    private Double mensuelSalary;

    @NotNull(message = "Role is required")
    private RoleEnum role;
}

