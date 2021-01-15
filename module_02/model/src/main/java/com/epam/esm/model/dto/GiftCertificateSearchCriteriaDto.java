package com.epam.esm.model.dto;

import com.epam.esm.model.domain.GiftCertificate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Validated
public class GiftCertificateSearchCriteriaDto implements SearchCriteriaDto<GiftCertificate> {

    private final List<@Pattern(regexp = "[\\w\\s]{3,256}", message = "tag name must be not blank, and contains " +
            "from 3 to 256 characters without punctuation marks") String> tags;
    @Pattern(regexp = "[\\w\\s]{3,256}", message = "certificate name must be not blank, and contains from 3 to 256 " +
            "characters without punctuation marks")
    private final String name;
    @Pattern(regexp = "[-,.!?\\w\\s]{3,1024}", message = "description must be not blank, and contains from 3 to 1024 " +
            "characters with punctuation marks")
    private final String description;
    private final List<@Pattern(regexp = "(name)|(create_date)|(last_update_date)") String> sortParams;
    @Pattern(regexp = "(DESC)|(ASC)")
    private final String order;

    private Class<GiftCertificate> targetClassType = GiftCertificate.class;

    @Override
    public Class<GiftCertificate> getTargetClassType() {
        return targetClassType;
    }
}
