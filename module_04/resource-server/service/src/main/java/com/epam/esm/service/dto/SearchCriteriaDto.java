package com.epam.esm.service.dto;

public interface SearchCriteriaDto<T> {

    Integer getPage();

    Integer getPageSize();

    void setPage(Integer page);

    void setPageSize(Integer pageSize);
}
