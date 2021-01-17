package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@Validated
public class GiftCertificateDto extends EntityDto<Long> {

    @NotBlank(message = "name must be not blank")
    @Pattern(regexp = "[\\-0-9A-Za-zА-Яа-яЁё ]{3,256}", message = "name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private String name;

    @NotBlank(message = "description must be not blank")
    @Pattern(regexp = "[\\-,.:!?0-9A-Za-zА-Яа-яЁё ]{3,1024}", message = "description must contain from 3 to 1024 " +
            "characters with punctuation marks")
    private String description;

    @NotNull(message = "price must not be null")
    @DecimalMin(value = "1.0", message = "minimal value of price is 1.0+")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "duration must not be null")
    @Positive(message = "duration must be positive")
    private Integer duration;

    @Null(message = "creation date must be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createDate;

    @Null(message = "updating date must be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updateDate;

    @NotEmpty(message = "tags must not be null nor empty")
    private List<@Valid TagDto> tags;
}
