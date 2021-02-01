package com.epam.esm.model.dto;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@Validated
public class OrderPositionDto extends GeneralEntityDto<Long> {

    private BigDecimal price;

    private Order order;

    private GiftCertificate giftCertificate;
}
