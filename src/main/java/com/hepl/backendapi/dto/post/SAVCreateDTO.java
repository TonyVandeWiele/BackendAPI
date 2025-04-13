package com.hepl.backendapi.dto.post;

import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SAVCreateDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;
}

