package com.hepl.backendapi.entity.dbtransac;

import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "client_account_number")
    private String clientAccountNumber;

    @Column(name = "birth_date")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private SexeEnum sexe;

    @Column(name = "monthly_salary")
    private Double mensuelSalary;
}
