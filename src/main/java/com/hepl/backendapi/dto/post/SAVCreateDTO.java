package com.hepl.backendapi.dto.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SAVCreateDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    @NotNull(message = "Description is required")
    private String description;
}

