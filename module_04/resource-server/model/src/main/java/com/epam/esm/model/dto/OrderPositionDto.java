package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(itemRelation = "orderPosition", collectionRelation = "orderPositions")
public class OrderPositionDto extends EntityDto<Long, OrderPositionDto> {

    @Null(message = "price of an order position cannot be specified by users")
    private BigDecimal price;

    @Null(message = "connected order is excess in an order position")
    private OrderDto order;

    @NotNull(message = "gift-certificate's id must be specified")
    private GiftCertificateDto giftCertificate;
}
