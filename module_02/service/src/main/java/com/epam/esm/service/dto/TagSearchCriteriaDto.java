package com.epam.esm.service.dto;

import com.epam.esm.model.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagSearchCriteriaDto implements SearchCriteriaDto<Tag> {

    @Pattern(regexp = "[A-Za-zА-Яа-яЁё ]{1,256}", message = "tag name must contain from 1 to 256 " +
            "characters without punctuation marks")
    private String name;

    @Null
    private Class<Tag> targetClassType = Tag.class;

    @Override
    public Class<Tag> getTargetClassType() {
        return targetClassType;
    }
}
