package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPositionDto extends GeneralEntityDto<Long> {

    private BigDecimal price;

    private OrderDto order;

    private GiftCertificateDto giftCertificate;
}
