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
@Table(name = "orders", schema = "bdtransac")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "order_time")
    private LocalTime orderTime;

    private Double total;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "tracking_id")
    private Long trackingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(name = "delivery_instruction")
    private String deliveryInstruction;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "livreur_id")
    private Long deliveryAgentId;
}
