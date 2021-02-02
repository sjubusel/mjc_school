package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@Validated
public class GiftCertificateUpdateDto extends GeneralEntityDto<Long, GiftCertificateUpdateDto> {
    @Null(message = "name cannot be updated, only price, description and tags are available for modification")
    private String name;

    @Null(message = "description cannot be updated, only price, description and tags are available for modification")
    private String description;

    @DecimalMin(value = "1.0", message = "minimal value of price is 1.0+")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal price;

    @Positive(message = "duration must be positive")
    private Integer duration;

    @Null(message = "creation datetime cannot be updated, only price, description and tags are available for modification")
    private Instant createDate;

    @Null(message = "update datetime cannot be changed, only price, description and tags are available for modification")
    private Instant updateDate;

    private Set<@Valid TagDto> tags;

    @Null(message = "delete status must be null")
    private Boolean isDeleted;

    @Null(message = "delete date must be null")
    private Instant deleteDate;
}
