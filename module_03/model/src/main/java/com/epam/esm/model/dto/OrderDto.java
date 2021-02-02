package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Validated
public class OrderDto extends GeneralEntityDto<Long, OrderDto> {

    @Null(message = "order price cannot be defined by users")
    private BigDecimal price;

    @Null(message = "order date cannot be defined by users")
    private Instant orderDate;

    @NotNull(message = "user's id must be specified")
    private UserDto user;

    @NotEmpty(message = "order positions must not be empty")
    private Set<@Valid OrderPositionDto> orderPositions;
}
