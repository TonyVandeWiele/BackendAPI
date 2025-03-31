package com.hepl.backendapi.dto.post;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateDTO {
    private String street;
    private String city;
    private String number;
    private String zipCode;
    private String country;
}
