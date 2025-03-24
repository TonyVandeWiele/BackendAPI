package com.hepl.backendapi.entity.dbtransac;

import com.hepl.backendapi.utils.compositekey.OrderLineId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "order_lines")
public class OrderLinesEntity {

    @EmbeddedId
    private OrderLineId id;



    @Column(name = "quantity")
    private int quantity;

    // Constructors, getters, setters
}
