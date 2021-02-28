package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.util.HateoasActionsAppender;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class OrderHateoasActionsAppender implements HateoasActionsAppender<Long, OrderDto> {

    @Override
    public void appendSelfReference(OrderDto dto) {
        dto.add(linkTo(OrderController.class).slash(dto.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(OrderDto dto) {
        appendAsForSecondaryEntity(dto);
        appendGenericOrderHateoasActions(dto);
    }

    @Override
    public void appendAsForSecondaryEntity(OrderDto dto) {
        appendSelfReference(dto);
    }

    @Override
    public CollectionModel<OrderDto> toHateoasCollectionOfEntities(List<OrderDto> orders) {
        orders.forEach(this::appendSelfReference);
        Link selfLink = linkTo(OrderController.class).withSelfRel();
        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders, selfLink);
        appendGenericOrderHateoasActions(collectionModel);
        return collectionModel;
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericOrderHateoasActions(RepresentationModel dto) {
        dto.add(linkTo(OrderController.class).withRel("POST: create a new order"));
        dto.add(linkTo(OrderController.class).withRel("GET: get all orders"));
    }
}
