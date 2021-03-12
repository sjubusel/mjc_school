package com.epam.esm.web.controller;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import com.epam.esm.web.util.impl.OrderHateoasActionsAppender;
import com.epam.esm.web.validation.ValidPage;
import com.epam.esm.web.validation.ValidPageSize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderHateoasActionsAppender hateoasActionsAppender;
    private final PageableSearchCriteriaAssembler<Order, Long> pageableSearchCriteriaAssembler;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and " +
            "((hasRole('USER') and ((#criteriaDto != null and #criteriaDto.userId != null) " +
            "? (#criteriaDto.userId == authentication.principal.claims['user_id']) " +
            ": false)) or hasRole('ADMIN'))")
    public CollectionModel<EntityModel<OrderDto>> read(@RequestBody(required = false)
                                                       @Valid OrderSearchCriteriaDto criteriaDto,
                                                       @RequestParam(required = false) @ValidPage Integer page,
                                                       @RequestParam(required = false) @ValidPageSize Integer size) {
        SearchCriteriaDto<Order> searchCriteria = pageableSearchCriteriaAssembler.toSearchCriteria(criteriaDto,
                page, size);
        Page<OrderDto> orders = orderService.query(searchCriteria);

        return hateoasActionsAppender.toHateoasCollectionOfEntities(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and (hasRole('USER') or hasRole('ADMIN'))")
    @PostAuthorize("returnObject.user.login == authentication.principal.claims['user_name']")
    public OrderDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        OrderDto order = orderService.findOne(id);

        hateoasActionsAppender.appendAsForMainEntity(order);

        return order;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_module_04::create') " +
            "and ((hasRole('USER') and #orderDto.user.id == authentication.principal.claims['user_id']) " +
            "or hasRole('ADMIN'))")
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderDto orderDto) {
        Long createdId = orderService.create(orderDto);
        URI location = URI.create(String.format("/orders/%s", createdId));
        return ResponseEntity.created(location).body(orderService.findOne(createdId));
    }
}
