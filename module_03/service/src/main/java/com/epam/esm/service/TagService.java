package com.epam.esm.service;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;

import java.util.List;

public interface TagService extends CrudService<TagDto, Tag, Long, TagDto> {

    List<TagDto> receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders();
}
