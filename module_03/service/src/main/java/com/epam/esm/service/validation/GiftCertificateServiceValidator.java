package com.epam.esm.service.validation;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class GiftCertificateServiceValidator implements ServiceValidator<GiftCertificate, Long> {

    @Override
    public boolean isDomainValidToUpdate(GiftCertificate updatingDomain) {
        List<Tag> updatingTags = new ArrayList<>(updatingDomain.getTags());
        return Stream.of(updatingDomain.getName(), updatingDomain.getDescription(), updatingDomain.getPrice(),
                updatingDomain.getDuration())
                .anyMatch(Objects::nonNull) || ((updatingTags != null) && (updatingTags.size() > 0)); // FIXME delete null check
    }
}
