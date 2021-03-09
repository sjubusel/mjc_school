package com.epam.esm.repository_new.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository_new.GeneralCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends GeneralCrudRepository<Tag, Long> {

    String RECEIVE_MOST_WIDELY_USED_TAG_OF_USER_WITH_MAX_COST_OF_ORDERS
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
            "                      JOIN certificates_tags j on j.certificate_id = op.certificate_id " +
            "                      JOIN tags t ON t.id = j.tag_id " +
            "             WHERE o.user_id IN (SELECT user_id " +
            "                                 FROM users " +
            "                                 WHERE sums = (SELECT MAX(sums) FROM users)) " +
            "             GROUP BY users.user_id, t.id, t.name, t.is_deleted, t.delete_date) " +
            "SELECT tu.id, tu.name, tu.is_deleted, tu.delete_date " +
            "FROM tags_by_users tu " +
            "WHERE tu.tags_number = (SELECT MAX(tags_number) FROM tags_by_users " +
            "                                                WHERE tu.user_id = tags_by_users.user_id)";

    @Query(value = RECEIVE_MOST_WIDELY_USED_TAG_OF_USER_WITH_MAX_COST_OF_ORDERS, nativeQuery = true)
    List<Tag> receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders();

    Long findIdByNameAndIsDeleted(String name, Boolean isDeleted);

    @Override
    @Modifying
    @Query("UPDATE #{#entityName} AS e SET e.isDeleted=true, e.deleteDate=CURRENT_TIMESTAMP WHERE e.id=?1")
    void deleteById(Long id);
}
