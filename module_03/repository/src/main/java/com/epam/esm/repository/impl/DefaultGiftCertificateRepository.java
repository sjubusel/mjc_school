package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.Set;

@Repository
public class DefaultGiftCertificateRepository extends GeneralCrudRepository<GiftCertificate, Long>
        implements GiftCertificateRepository {

    @Autowired
    protected DefaultGiftCertificateRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected CriteriaQuery<GiftCertificate> getCriteriaQueryReadById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        root.join("tags", JoinType.LEFT);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        criteriaQuery.select(root);

        return criteriaQuery;
    }

    @Override
    public void linkGiftCertificateWithTags(Long createdId, Set<Tag> updatingTags) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, createdId);

        updatingTags.forEach(tag -> {
            if (tag.getId() == null) {
                Long tagId = entityManager.createQuery("SELECT t.id FROM Tag t WHERE t.name=:name ",
                        Long.class).setParameter("name", tag.getName()).getSingleResult();
                tag.setId(tagId);
            }
        });

        giftCertificate.setTags(updatingTags);
        entityManager.merge(giftCertificate);
    }

    @Override
    public void deleteLinkBetweenGiftCertificateAndTags(GiftCertificate certificate) {
        certificate.setTags(null);
        entityManager.merge(certificate);
    }

    @Override
    protected CriteriaQuery<GiftCertificate> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        Predicate nameCondition = criteriaBuilder.equal(root.get("name"), uniqueConstraints.get("name"));
        Predicate descriptionCondition = criteriaBuilder.equal(root.get("description"),
                uniqueConstraints.get("description"));
        Predicate priceCondition = criteriaBuilder.equal(root.get("price"), uniqueConstraints.get("price"));
        Predicate durationCondition = criteriaBuilder.equal(root.get("duration"), uniqueConstraints.get("duration"));
        Predicate finalPredicate = criteriaBuilder.and(nameCondition, descriptionCondition, priceCondition,
                durationCondition);

        return criteriaQuery.select(root).where(finalPredicate);
    }

    @Override
    protected Query getDeleteQuery(Long idToDelete) {
        return entityManager.createQuery("DELETE FROM GiftCertificate AS certificate WHERE certificate.id=:id")
                .setParameter("id", idToDelete);
    }

}
