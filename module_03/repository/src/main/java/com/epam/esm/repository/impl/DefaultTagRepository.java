package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DefaultTagRepository extends GeneralCrudRepository<Tag, Long> implements TagRepository {

    public static final String RECEIVE_MOST_WIDELY_USED_TAG_OF_USER_WITH_MAX_COST_OF_ORDERS
            = "WITH users AS (SELECT user_id, SUM(op.price) AS sums " +
            "               FROM orders o " +
            "                        JOIN order_positions op ON o.id = op.order_id " +
            "               GROUP BY user_id " +
            "), " +
            "     tags_by_users AS " +
            "         ( " +
            "             SELECT users.user_id, t.id, t.name, t.is_deleted, t.delete_date," +
            "                    COUNT(t.name) as tags_number " +
            "             FROM users " +
            "                      JOIN orders o ON o.user_id = users.user_id " +
            "                      JOIN order_positions op ON op.order_id = o.id " +
            "                      JOIN join_certificates_tags_table j on j.certificate_id = op.certificate_id " +
            "                      JOIN tags t ON t.id = j.tag_id " +
            "             WHERE o.user_id IN (SELECT user_id " +
            "                                 FROM users " +
            "                                 WHERE sums = (SELECT MAX(sums) FROM users)) " +
            "             GROUP BY users.user_id, t.id, t.name, t.is_deleted, t.delete_date) " +
            "SELECT tu.id, tu.name, tu.is_deleted, tu.delete_date " +
            "FROM tags_by_users tu " +
            "WHERE tu.tags_number = (SELECT MAX(tags_number) FROM tags_by_users " +
            "                                                WHERE tu.user_id = tags_by_users.user_id)";

    @Autowired
    protected DefaultTagRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected CriteriaQuery<Tag> getCriteriaQueryReadById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> root = criteriaQuery.from(Tag.class);
        Predicate condition = criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id),
                criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE));
        criteriaQuery.where(condition);
        criteriaQuery.select(root);

        return criteriaQuery;
    }

    @Override
    public List<Tag> receiveTagsByGiftCertificateId(Long id) {
        return null; // fixme
    }

    @Override
    public void deleteLinkBetweenTagAndGiftCertificates(Tag tag) {
        entityManager.createNativeQuery("DELETE FROM join_certificates_tags_table WHERE tag_id=:id")
                .setParameter("id", tag.getId())
                .executeUpdate();
    }

    @Override
    public boolean exists(String uniqueConstraint) {
        HashMap<String, Object> uniqueConstraints = new HashMap<>();
        uniqueConstraints.putIfAbsent("name", uniqueConstraint);
        return exists(uniqueConstraints);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tag> receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders() {
        return (List<Tag>) entityManager.createNativeQuery(RECEIVE_MOST_WIDELY_USED_TAG_OF_USER_WITH_MAX_COST_OF_ORDERS,
                Tag.class).getResultList();
    }

    @Override
    protected CriteriaQuery<Tag> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        Predicate nameCondition = criteriaBuilder.equal(root.get("name"), uniqueConstraints.get("name"));
        Predicate existsCondition = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
        Predicate finalCondition = criteriaBuilder.and(nameCondition, existsCondition);

        return criteriaQuery.select(root).where(finalCondition);
    }

    @Override
    protected Query getDeleteQuery(Long idToDelete) {
        return entityManager.createQuery("UPDATE Tag AS tag SET tag.isDeleted=:isDeleted, " +
                "tag.deleteDate=:deleteDate WHERE tag.id=:id")
                .setParameter("isDeleted", Boolean.TRUE)
                .setParameter("deleteDate", Instant.now())
                .setParameter("id", idToDelete);
    }

}
