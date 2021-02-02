package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public CollectionModel<OrderDto> read(@RequestBody(required = false) @Valid OrderSearchCriteriaDto criteriaDto) {
        List<OrderDto> orders = orderService.query(criteriaDto);

        orders.forEach(order -> order.add(linkTo(OrderController.class).slash(order.getId()).withSelfRel()));
        Link selfLink = linkTo(methodOn(OrderController.class).read(criteriaDto)).withSelfRel();
        Link newOrderLink = linkTo(OrderController.class).withRel("POST: create a new order");

        return CollectionModel.of(orders, selfLink, newOrderLink);
    }

    @GetMapping("/{id}")
    public OrderDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        OrderDto order = orderService.findOne(id);

        order.add(linkTo(OrderController.class).slash(order.getId()).withSelfRel());
        order.add(linkTo(OrderController.class).withRel("GET: get all orders"));
        order.add(linkTo(OrderController.class).withRel("POST: create a new order"));

        return order;
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderDto orderDto) {
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }
}
