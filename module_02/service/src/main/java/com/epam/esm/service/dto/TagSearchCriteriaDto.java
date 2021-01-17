package com.epam.esm.service.dto;

import com.epam.esm.model.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagSearchCriteriaDto implements SearchCriteriaDto<Tag> {

    @Pattern(regexp = "[0-9A-Za-zА-Яа-яЁё ]{1,256}", message = "tag name must contain from 1 to 256 " +
            "characters without punctuation marks")
    private String name;
}
