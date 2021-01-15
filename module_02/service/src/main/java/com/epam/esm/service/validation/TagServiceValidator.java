package com.epam.esm.service.validation;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagServiceValidator extends BasicServiceValidator<TagDto, Tag, Long> {

    @Override
    public boolean isDomainValidToUpdate(Tag updatingDomain) {
        return updatingDomain.getName() != null;
    }
}
