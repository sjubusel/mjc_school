package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> read(@RequestBody(required = false) @Valid OrderSearchCriteriaDto criteriaDto) {
        return orderService.query(criteriaDto);
    }

    @GetMapping("/{id}")
    public OrderDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return orderService.findOne(id);
    }
}
