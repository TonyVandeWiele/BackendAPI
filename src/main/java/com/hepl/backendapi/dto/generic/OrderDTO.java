package com.hepl.backendapi.dto.generic;

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

    private LocalDate order_date;

    private LocalTime order_time;

    private StatusEnum status;

    private String bank_name;

    private Float total;

    private TrackingDTO tracking;

    private List<Long> productsId;

    private AddressDTO address;

    private Long client_id;
}
