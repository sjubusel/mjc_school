package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@NoArgsConstructor
public class TagSpecification implements JpaSpecification<Tag, Long> {
    private String name;

    public TagSpecification(String name) {
        this.name = name;
    }

    @Override
    public TypedQuery<Tag> toQuery(EntityManager entityManager) {
        return null;
    }
}
