package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto extends EntityDto<Long, TagDto> {

    @NotBlank(message = "tag name must be not blank")
    @Pattern(regexp = "[:\\-0-9A-Za-zА-Яа-яЁё ]{3,256}", message = "tag name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private String name;

    @Null(message = "gift certificates cannot be defined by users")
    private Set<GiftCertificateDto> giftCertificates;

    @Null(message = "tag status cannot be defined by users")
    private Boolean isDeleted;

    @Null(message = "delete date cannot be defined by users")
    private Instant deleteDate;
}
