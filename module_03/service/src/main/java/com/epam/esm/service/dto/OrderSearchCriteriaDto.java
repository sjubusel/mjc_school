package com.epam.esm.service.dto;

import com.epam.esm.model.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchCriteriaDto implements SearchCriteriaDto<Order> {

    @Min(value = 1, message = "page must be greater than 1")
    @Digits(integer = 20, fraction = 0)
    private Integer page;
}
