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

    private List<@Pattern(regexp = "[:\\-0-9A-Za-zА-Яа-яЁё ]{1,256}", message = "tag name must contain " +
            "from 1 to 256 characters without punctuation marks") String> tags;

    @Pattern(regexp = "[0-9A-Za-zА-Яа-яЁё ]{1,256}", message = "certificate name must contain from 1 to 256 " +
            "characters without punctuation marks")
    private String namePart;

    @Pattern(regexp = "[-,.:!?0-9A-Za-zА-Яа-яЁё]{1,1024}", message = "description must contain from 1 to 1024 " +
            "characters with punctuation marks")
    private String descriptionPart;

    private List<@Pattern(regexp = "([-+]name)|([-+]createDate)|([-+]updateDate)")
            String> sortParams;

    @Null(message = "page must be specified as a query parameter")
    private Integer page;

    @Null(message = "page size must be specified as a query parameter")
    private Integer pageSize;
}
