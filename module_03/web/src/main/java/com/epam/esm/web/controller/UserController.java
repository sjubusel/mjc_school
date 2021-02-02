package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public List<UserDto> read(@RequestBody(required = false) @Valid UserSearchCriteriaDto criteriaDto) {
        return userService.query(criteriaDto);
    }

    @GetMapping("/{id}")
    public UserDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return userService.findOne(id);
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("id") @Positive @Min(1) Long id,
                                                @RequestBody @Valid OrderDto orderDto) {
        orderDto.getUser().setId(id);
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> readOrders(@PathVariable("id") @Positive @Min(1) Long id,
                                     @RequestBody @Valid OrderSearchCriteriaDto criteriaDto) {
        criteriaDto.setUserId(id);
        return orderService.query(criteriaDto);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto readOrder(@PathVariable("userId") @Positive @Min(1) Long userId,
                                    @PathVariable("orderId") @Positive @Min(1) Long orderId) {

        return orderService.findOrderByIdIfBelongsToUser(orderId, userId);
    }
}
