package com.hepl.backendapi.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private int id;

    private LocalDate order_date;

    private LocalDateTime order_time;

    private float total;

    private String status;

    private String bank_name;

    private String tracking_id;
}
