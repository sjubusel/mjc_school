package com.epam.esm.model.dto;

import com.epam.esm.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
public class OrderDto extends GeneralEntityDto<Long> {

    private BigDecimal price;

    private Instant orderDate;

    private User user;

    private Set<OrderPositionDto> orderPositions;
}
