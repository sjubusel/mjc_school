package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.web.controller.GiftCertificateController;
import com.epam.esm.web.util.HateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
@RequiredArgsConstructor
public class GiftCertificateHateoasActionsAppender implements HateoasActionsAppender<Long, GiftCertificateDto> {

    private final TagHateoasActionsAppender tagHateoasActionsAppender;
    private final PagedResourcesAssembler<GiftCertificateDto> pagedResourcesAssembler;

    @Override
    public void appendSelfReference(GiftCertificateDto giftCertificate) {
        giftCertificate.add(linkTo(GiftCertificateController.class).slash(giftCertificate.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(GiftCertificateDto giftCertificate) {
        appendAsForSecondaryEntity(giftCertificate);
        appendGenericCreateAndReadAllHateoasActions(giftCertificate);
    }

    @Override
    public void appendAsForSecondaryEntity(GiftCertificateDto giftCertificate) {
        appendSelfReference(giftCertificate);
        giftCertificate.add(linkTo(GiftCertificateController.class).slash(giftCertificate.getId())
                .withRel("PATCH: update a current gift-certificate"));
        giftCertificate.add(linkTo(GiftCertificateController.class).slash(giftCertificate.getId())
                .withRel("DELETE: delete a current gift-certificate"));

        giftCertificate.getTags().forEach(tagHateoasActionsAppender::appendSelfReference);
    }

    @Override
    public CollectionModel<EntityModel<GiftCertificateDto>> toHateoasCollectionOfEntities(Page<GiftCertificateDto>
                                                                                                      certificates) {
        certificates.forEach(this::appendAsForSecondaryEntity);
        PagedModel<EntityModel<GiftCertificateDto>> collectionModel = pagedResourcesAssembler.toModel(certificates);
        appendGenericCreateAndReadAllHateoasActions(collectionModel);
        return collectionModel;
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericCreateAndReadAllHateoasActions(RepresentationModel model) {
        model.add(linkTo(GiftCertificateController.class).withRel("POST: create a new gift-certificate"));
        model.add(linkTo(GiftCertificateController.class).withRel("GET: receive all gift-certificates"));
    }
}
