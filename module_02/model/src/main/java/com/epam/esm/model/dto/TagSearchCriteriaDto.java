package com.epam.esm.model.dto;

import com.epam.esm.model.domain.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class TagSearchCriteriaDto implements SearchCriteriaDto<Tag> {

    @Pattern(regexp = "[\\w\\s]{3,256}", message = "tag name must contain from 3 to 256 " +
            "characters without punctuation marks")
    private final String name;

    @Null
    private Class<Tag> targetClassType = Tag.class;

    @Override
    public Class<Tag> getTargetClassType() {
        return targetClassType;
    }
}
