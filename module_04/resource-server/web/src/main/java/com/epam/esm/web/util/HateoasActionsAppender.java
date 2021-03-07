package com.epam.esm.web.util;

import com.epam.esm.model.dto.EntityDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;

import java.io.Serializable;

public interface HateoasActionsAppender<ID extends Serializable, T extends EntityDto<ID, T>> {

    void appendSelfReference(T dto);

    void appendAsForMainEntity(T dto);

    void appendAsForSecondaryEntity(T dto);

    // TODO ??? PagedModel
    CollectionModel<T> toHateoasCollectionOfEntities(Page<T> dtoList);
}
