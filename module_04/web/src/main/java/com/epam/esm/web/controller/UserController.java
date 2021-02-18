package com.epam.esm.web.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import com.epam.esm.web.util.impl.UserHateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final OrderService orderService;
    private final UserService userService;
    private final UserHateoasActionsAppender hateoasActionsAppender;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CollectionModel<UserDto> read(@RequestBody(required = false) @Valid UserSearchCriteriaDto criteriaDto) {
        List<UserDto> users = userService.query(criteriaDto);

        return hateoasActionsAppender.toHateoasCollectionOfEntities(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        UserDto user = userService.findOne(id);

        hateoasActionsAppender.appendAsForMainEntity(user);

        return user;
    }

    @PostMapping("/{id}/orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("id") @Positive @Min(1) Long id,
                                                @RequestBody @Valid OrderDto orderDto) {
        orderDto.getUser().setId(id);
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<OrderDto> readOrders(@PathVariable("id") @Positive @Min(1) Long id,
                                     @RequestBody(required = false) @Valid OrderSearchCriteriaDto criteriaDto) {
        if (criteriaDto == null) {
            criteriaDto = new OrderSearchCriteriaDto();
        }
        criteriaDto.setUserId(id);
        List<OrderDto> orders = orderService.query(criteriaDto);
        return hateoasActionsAppender.toHateoasCollectionOfOrders(orders);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto readOrder(@PathVariable("userId") @Positive @Min(1) Long userId,
                              @PathVariable("orderId") @Positive @Min(1) Long orderId) {

        OrderDto order = orderService.findOrderByIdIfBelongsToUser(orderId, userId);
        order.add(linkTo(OrderController.class).slash(order.getId()).withSelfRel());
        return order;
    }

}
