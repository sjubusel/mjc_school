package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.web.util.impl.OrderHateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderHateoasActionsAppender hateoasActionsAppender;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CollectionModel<OrderDto> read(@RequestBody(required = false) @Valid OrderSearchCriteriaDto criteriaDto) {
        List<OrderDto> orders = orderService.query(criteriaDto);

        return hateoasActionsAppender.toHateoasCollectionOfEntities(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        OrderDto order = orderService.findOne(id);

        hateoasActionsAppender.appendAsForMainEntity(order);

        return order;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderDto orderDto) {
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }
}