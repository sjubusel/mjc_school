package com.epam.esm.web.util;

import com.epam.esm.model.dto.EntityDto;
import org.springframework.hateoas.CollectionModel;

import java.io.Serializable;
import java.util.List;

public interface HateoasActionsAppender<ID extends Serializable, T extends EntityDto<ID, T>> {

    void appendSelfReference(T dto);

    void appendAsForMainEntity(T dto);

    void appendAsForSecondaryEntity(T dto);

    CollectionModel<T> toHateoasCollectionOfEntities(List<T> dtoList);
}
