package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.util.impl.GiftCertificatePredicateBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Repository
public class GiftCertificateRepositoryImpl extends GeneralCrudRepository<GiftCertificate, Long>
        implements GiftCertificateRepository {

    private final GiftCertificatePredicateBuilder predicateBuilder;

    protected GiftCertificateRepositoryImpl(EntityManager entityManager,
                                            GiftCertificatePredicateBuilder predicateBuilder) {
        super(entityManager);
        this.predicateBuilder = predicateBuilder;
    }

    @Override
    protected CriteriaQuery<GiftCertificate> getCriteriaQueryReadById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        root.join("tags", JoinType.LEFT);
        Predicate condition = criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id),
                criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE));
        criteriaQuery.where(condition);
        criteriaQuery.select(root);

        return criteriaQuery;
    }

    @Override
    public void linkGiftCertificateWithTags(Long createdId, Set<Tag> updatingTags) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, createdId);
        giftCertificate.setTags(updatingTags);
    }

    @Override
    protected CriteriaQuery<GiftCertificate> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Predicate finalPredicate = predicateBuilder.buildExistsPredicate(criteriaBuilder, root, uniqueConstraints);

        return criteriaQuery.select(root).where(finalPredicate);
    }

    @Override
    protected Query getDeleteQuery(Long idToDelete) {
        return entityManager.createQuery("UPDATE GiftCertificate AS gc SET gc.isDeleted=:isDeleted, " +
                "gc.deleteDate=:deleteDate WHERE gc.id=:id")
                .setParameter("isDeleted", Boolean.TRUE)
                .setParameter("deleteDate", Instant.now())
                .setParameter("id", idToDelete);
    }

}
