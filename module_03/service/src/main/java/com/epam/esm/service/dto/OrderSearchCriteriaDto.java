package com.epam.esm.service.dto;

import com.epam.esm.model.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class OrderSearchCriteriaDto implements SearchCriteriaDto<Order> {

    @Min(value = 1, message = "page must be greater than or equal 1")
    @Digits(integer = 20, fraction = 0)
    private Integer page;

    @Min(value = 1, message = "user's id must be greater than or equal 1")
    @Digits(integer = 20, fraction = 0)
    private Long userId;
}
