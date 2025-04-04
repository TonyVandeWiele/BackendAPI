package com.hepl.backendapi.dto.generic;

import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import com.hepl.backendapi.utils.enumeration.StatusEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private int id;

    private LocalDate orderDate;

    private LocalTime orderTime;

    private StatusEnum status;

    private String bankName;

    private Float total;

    private TrackingDTO tracking;

    private List<OrderItemDTO> orderItems;

    private AddressDTO address;

    private Long clientId;
}
