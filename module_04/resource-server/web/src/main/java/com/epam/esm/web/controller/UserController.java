package com.epam.esm.web.controller;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import com.epam.esm.web.util.impl.UserHateoasActionsAppender;
import com.epam.esm.web.validation.ValidPage;
import com.epam.esm.web.validation.ValidPageSize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final OrderService orderService;
    private final UserService userService;
    private final UserHateoasActionsAppender hateoasActionsAppender;
    private final PageableSearchCriteriaAssembler<User, Long> userPageableSearchCriteriaAssembler;
    private final PageableSearchCriteriaAssembler<Order, Long> orderPageableSearchCriteriaAssembler;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and hasRole('ADMIN')")
    public CollectionModel<EntityModel<UserDto>> read(@RequestBody(required = false)
                                                      @Valid UserSearchCriteriaDto criteriaDto,
                                                      @RequestParam(required = false)
                                                      @ValidPage Integer page,
                                                      @RequestParam(required = false)
                                                      @ValidPageSize Integer size) {
        SearchCriteriaDto<User> searchCriteria = userPageableSearchCriteriaAssembler
                .toSearchCriteria(criteriaDto, page, size);
        Page<UserDto> users = userService.query(searchCriteria);

        return hateoasActionsAppender.toHateoasCollectionOfEntities(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') " +
            "and ((hasRole('USER') and #id == authentication.principal.claims['user_id']) or hasRole('ADMIN'))")
    public UserDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        UserDto user = userService.findOne(id);

        hateoasActionsAppender.appendAsForMainEntity(user);

        return user;
    }

    @PostMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('SCOPE_module_04::create') " +
            "and ((hasRole('USER') and #id == authentication.principal.claims['user_id']) or hasRole('ADMIN'))")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("id") @Positive @Min(1) Long id,
                                                @RequestBody @Valid OrderDto orderDto) {
        orderDto.getUser().setId(id);
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') " +
            "and ((hasRole('USER') and #id == authentication.principal.claims['user_id']) or hasRole('ADMIN'))")
    public CollectionModel<EntityModel<OrderDto>> readOrders(@PathVariable("id") @Positive @Min(1) Long id,
                                                             @RequestBody(required = false)
                                                             @Valid OrderSearchCriteriaDto criteriaDto,
                                                             @RequestParam(required = false)
                                                             @ValidPage Integer page,
                                                             @RequestParam(required = false)
                                                             @ValidPageSize Integer size) {
        OrderSearchCriteriaDto searchCriteria = (OrderSearchCriteriaDto) orderPageableSearchCriteriaAssembler
                .toSearchCriteria(criteriaDto, page, size);
        searchCriteria.setUserId(id);
        Page<OrderDto> orders = orderService.query(searchCriteria);
        return hateoasActionsAppender.toHateoasCollectionOfOrders(orders);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read')" +
            "and ((hasRole('USER') and #userId == authentication.principal.claims['user_id']) or hasRole('ADMIN'))")
    public OrderDto readOrder(@PathVariable("userId") @Positive @Min(1) Long userId,
                              @PathVariable("orderId") @Positive @Min(1) Long orderId) {

        OrderDto order = orderService.findOrderByIdIfBelongsToUser(orderId, userId);
        order.add(linkTo(OrderController.class).slash(order.getId()).withSelfRel());
        return order;
    }

}
