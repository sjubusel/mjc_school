package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
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
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public CollectionModel<UserDto> read(@RequestBody(required = false) @Valid UserSearchCriteriaDto criteriaDto) {
        List<UserDto> users = userService.query(criteriaDto);

        users.forEach(this::applyHateoasActionsForSeparateUser);
        Link selfLink = linkTo(methodOn(UserController.class).read(criteriaDto)).withSelfRel();
        Link postLink = linkTo(methodOn(UserController.class).createOrder(0L, new OrderDto()))
                .withRel("POST: create a new order for a user with id 0 (change )");

        return CollectionModel.of(users, selfLink, postLink);
    }

    @GetMapping("/{id}")
    public UserDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        UserDto user = userService.findOne(id);

        applyHateoasActionsForSeparateUser(user);
        user.add(linkTo(UserController.class).withRel("GET: receive all users"));
        user.add(linkTo(methodOn(UserController.class).createOrder(id, new OrderDto()))
                .withRel("POST: create a new order for a current user"));

        return user;
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
                                     @RequestBody(required = false) @Valid OrderSearchCriteriaDto criteriaDto) {
        if (criteriaDto == null) {
            criteriaDto = new OrderSearchCriteriaDto();
        }
        criteriaDto.setUserId(id);
        return orderService.query(criteriaDto);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto readOrder(@PathVariable("userId") @Positive @Min(1) Long userId,
                              @PathVariable("orderId") @Positive @Min(1) Long orderId) {

        return orderService.findOrderByIdIfBelongsToUser(orderId, userId);
    }

    private void applyHateoasActionsForSeparateUser(UserDto user) {
        user.add(linkTo(UserController.class).slash(user.getId()).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).readOrders(user.getId(), new OrderSearchCriteriaDto()))
                .withRel("GET: receive all orders for a current user"));
        user.add(linkTo(methodOn(UserController.class).readOrder(user.getId(), 0L))
                .withRel("GET: receive an order with a number instead of 0 if it belongs to a current user"));
    }
}
