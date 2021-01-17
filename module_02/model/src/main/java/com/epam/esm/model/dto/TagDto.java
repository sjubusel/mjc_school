package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
public class TagDto extends EntityDto<Long> {

    @NotBlank(message = "tag name must be not blank")
    @Pattern(regexp = "[\\-0-9A-Za-zА-Яа-яЁё ]{3,256}", message = "tag name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private String name;
}
