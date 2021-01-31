package com.epam.esm.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user", "orderPositions"})
@ToString(callSuper = true, exclude = {"user", "orderPositions"})
@SuperBuilder(setterPrefix = "set")
public class Order extends GeneralEntity<Long> {

    @Transient
    private BigDecimal price;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP")
    private Instant orderDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private Set<OrderPosition> orderPositions;
}
