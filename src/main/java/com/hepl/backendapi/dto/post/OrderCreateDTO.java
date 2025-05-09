package com.hepl.backendapi.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderCreateDTO {
    @Valid
    private List<OrderItemCreateDTO> orderItemsCreateDTOList;

    @Schema(description = "ID of the existing address (optional if providing a new address)", example = "5")
    private Long adresseId; // Pour une adresse existante

    @Valid
    @Schema(description = "New address details (optional if an address ID is provided)")
    private AddressCreateDTO newAddress; // Pour une nouvelle adresse

    @Size(max = 1800, message = "Delivery Instruction must be at most 1800 characters")
    private String deliveryInstruction;

}
