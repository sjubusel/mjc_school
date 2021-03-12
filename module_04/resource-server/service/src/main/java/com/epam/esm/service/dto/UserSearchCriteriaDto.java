package com.epam.esm.service.dto;

import com.epam.esm.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaDto implements SearchCriteriaDto<User> {

    @Null(message = "page must be specified as a query parameter")
    private Integer page;

    @Null(message = "page size must be specified as a query parameter")
    private Integer pageSize;
}
