package com.epam.esm.repository;

import com.epam.esm.model.domain.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    boolean exists(String uniqueConstraint);

    List<Tag> receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders();

    void refreshStateOfTagsByTheirName(List<Tag> tagsToRefresh);
}
