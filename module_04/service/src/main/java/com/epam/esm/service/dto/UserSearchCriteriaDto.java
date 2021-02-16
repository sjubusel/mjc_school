package com.epam.esm.service.dto;

import com.epam.esm.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaDto implements SearchCriteriaDto<User> {

    @Min(value = 1, message = "page must be greater than 1")
    @Digits(integer = 20, fraction = 0)
    private Integer page;

    @Min(value = 1, message = "page size must be greater than 1")
    @Digits(integer = 20, fraction = 0)
    private Integer pageSize;
}
