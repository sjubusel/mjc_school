package com.epam.esm.service.dto;

import com.epam.esm.model.domain.GiftCertificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class GiftCertificateSearchCriteriaDto implements SearchCriteriaDto<GiftCertificate> {

    private List<@Pattern(regexp = "[\\w\\s]{1,256}", message = "tag name must contain " +
            "from 3 to 256 characters without punctuation marks") String> tags;

    @Pattern(regexp = "[\\w\\s]{1,256}", message = "certificate name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private String name;

    @Pattern(regexp = "[-,.:!?\\w\\s]{1,1024}", message = "description must contain from 3 to 1024 " +
            "characters with punctuation marks")
    private String description;

    private List<@Pattern(regexp = "(name)|(create_date)|(last_update_date)") String> sortParams;

    @Pattern(regexp = "(DESC)|(ASC)")
    private String order;

    @Null
    private Class<GiftCertificate> targetClassType = GiftCertificate.class;

    @Override
    public Class<GiftCertificate> getTargetClassType() {
        return targetClassType;
    }
}
