package com.epam.esm.service.validation;

import com.epam.esm.model.domain.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagServiceValidator implements ServiceValidator<Tag, Long> {

    @Override
    public boolean isDomainValidToUpdate(Tag updatingDomain) {
        return updatingDomain.getName() != null;
    }
}
