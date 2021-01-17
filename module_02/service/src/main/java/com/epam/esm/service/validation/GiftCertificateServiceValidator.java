package com.epam.esm.service.validation;

import com.epam.esm.model.domain.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class GiftCertificateServiceValidator implements ServiceValidator<GiftCertificate, Long> {

    @Override
    public boolean isDomainValidToUpdate(GiftCertificate updatingDomain) {
        return Stream.of(updatingDomain.getName(), updatingDomain.getDescription(), updatingDomain.getPrice(),
                updatingDomain.getDuration())
                .anyMatch(Objects::nonNull) || updatingDomain.getTags().size() > 0;
    }
}
