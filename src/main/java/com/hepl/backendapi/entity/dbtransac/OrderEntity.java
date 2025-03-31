package com.hepl.backendapi.entity.dbtransac;

import com.hepl.backendapi.utils.enumeration.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders", schema = "dbtransac")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDate order_date;

    @Column(name = "order_time")
    private LocalTime order_time;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "bank_name")
    private String bank_name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tracking_id", referencedColumnName = "id")
    private TrackingEntity tracking;

    @Column(name = "address_id")
    private Long address_id;

    @Column(name = "client_id")
    private Long client_id;
}
