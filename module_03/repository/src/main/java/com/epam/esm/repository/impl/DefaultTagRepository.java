package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

@Repository
public class DefaultTagRepository extends GeneralCrudRepository<Tag, Long> implements TagRepository {

    @Autowired
    protected DefaultTagRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected CriteriaQuery<Tag> getCriteriaQueryReadById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        criteriaQuery.select(root);

        return criteriaQuery;
    }

    @Override
    public List<Tag> receiveTagsByGiftCertificateId(Long id) {
        return null; // fixme
    }

    @Override
    public void deleteLinkBetweenTagAndGiftCertificates(Long id) {
        // fixme
    }

    @Override
    public boolean exists(String uniqueConstraint) {
        return false; // fixme
    }

    @Override
    protected CriteriaQuery<Tag> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        Predicate nameCondition = criteriaBuilder.equal(root.get("name"), uniqueConstraints.get("name"));

        return criteriaQuery.select(root).where(nameCondition);
    }
}
