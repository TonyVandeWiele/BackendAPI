package com.hepl.backendapi.entity.dbservices;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "adresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "rue")
    private String street;

    @Column(name = "Ville")
    private String city;

    @Column(name = "Numero")
    private String number;

    @Column(name = "code_postal")
    private String zipCode;

    @Column(name = "Pays")
    private String country;
}
