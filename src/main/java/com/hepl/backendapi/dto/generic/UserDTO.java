package com.hepl.backendapi.dto.generic;


import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private AddressDTO address;
    private LocalDateTime registrationDate;
    private String clientAccountNumber;
    private SexeEnum sexe;
    private LocalDate birthday;
    private MaritalStatus maritalStatus;
    private Double mensuelSalary;
    private String role;
}
