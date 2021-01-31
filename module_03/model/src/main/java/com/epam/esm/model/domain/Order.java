package com.epam.esm.model.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "user")
@ToString(callSuper = true, exclude = "user")
@SuperBuilder(setterPrefix = "set")
public class Order extends GeneralEntity<Long> {

    @Transient
    private BigDecimal cost;

    @Column(name = "purchase_date", columnDefinition = "TIMESTAMP")
    private Instant purchaseDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
