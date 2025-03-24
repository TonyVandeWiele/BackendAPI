package com.hepl.backendapi.entity.dbservices;

import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String name;

    private String email;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(name = "date_inscription")
    private LocalDateTime inscriptionDate;

    @Column(name = "num_compte_client")
    private String clientAccountNumber;


    @Column(name = "date_naissance")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_civil")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "Sexe")
    private SexeEnum sexe;

    @Column(name = "salaire_mensuel")
    private BigDecimal mensuelSalary;

}
