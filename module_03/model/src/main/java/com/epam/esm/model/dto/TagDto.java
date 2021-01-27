package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto extends GeneralEntityDto<Long> {

    @NotBlank(message = "tag name must be not blank")
    @Pattern(regexp = "[\\-0-9A-Za-zА-Яа-яЁё ]{3,256}", message = "tag name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private String name;

    @Null
    private Set<GiftCertificateDto> giftCertificates;
}
