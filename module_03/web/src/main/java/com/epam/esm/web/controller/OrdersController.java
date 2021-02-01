package com.epam.esm.web.controller;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
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
public class OrdersController {

    private final OrderService orderService;

    @GetMapping
    public List<UserDto> read(@RequestBody(required = false) @Valid UserSearchCriteriaDto criteriaDto) {
        return orderService.query(criteriaDto);
    }

    @GetMapping("/{id}")
    public UserDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return orderService.findOne(id);
    }
}
