package com.hepl.backendapi.dto.generic;

import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SAVDTO {

    private Long id;

    private Long customerId;

    private Long orderId;

    private RequestTypeEnum requestType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
