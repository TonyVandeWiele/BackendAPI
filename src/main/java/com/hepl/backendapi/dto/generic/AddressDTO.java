package com.hepl.backendapi.dto.generic;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String number;
    private String zipCode;
    private String country;
}
