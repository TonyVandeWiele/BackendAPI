package com.hepl.backendapi.dto.update;

import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Phone number must be valid")
    private String phone;

    private AddressCreateDTO address;

    @Past(message = "Birthdate must be in the past")
    private LocalDate birthday;

    private MaritalStatus maritalStatus;

    private SexeEnum sexe;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly salary must be positive")
    private Double mensuelSalary;
}
