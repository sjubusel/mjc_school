package com.epam.esm.service.dto;

import com.epam.esm.model.domain.GiftCertificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class GiftCertificateSearchCriteriaDto implements SearchCriteriaDto<GiftCertificate> {

    private List<@Pattern(regexp = "[0-9A-Za-zА-Яа-яЁё ]{1,256}", message = "tag name must contain " +
            "from 1 to 256 characters without punctuation marks") String> tags;

    @Pattern(regexp = "[0-9A-Za-zА-Яа-яЁё ]{1,256}", message = "certificate name must contain from 1 to 256 " +
            "characters without punctuation marks")
    private String namePart;

    @Pattern(regexp = "[-,.:!?0-9A-Za-zА-Яа-яЁё]{1,1024}", message = "description must contain from 1 to 1024 " +
            "characters with punctuation marks")
    private String descriptionPart;

    private List<@Pattern(regexp = "((asc)|(desc)name)|((asc)|(desc)create_date)|((asc)|(desc)last_update_date)")
            String> sortParams;

    @Min(value = 1, message = "page must be greater than 1")
    @Digits(integer = 20, fraction = 0)
    private Long page;
}
