package com.hepl.backendapi.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderCreateDTO {
    private List<OrderItemCreateDTO> orderItemsCreateDTOList;

    @Schema(description = "ID of the existing address (optional if providing a new address)", example = "5")
    private Long adresseId; // Pour une adresse existante

    @Schema(description = "New address details (optional if an address ID is provided)")
    private AddressCreateDTO newAddress; // Pour une nouvelle adresse

}
