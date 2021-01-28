package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.*;
import java.util.stream.Stream;

@NoArgsConstructor
public class GiftCertificateSpecification implements JpaSpecification<GiftCertificate, Long> {
    private static final Integer PAGE_SIZE = 20;

    private List<Predicate> whereConditions;
    private List<Order> orderConditions;
    private Map<String, String> jpaTagParameters;

    private List<String> tags;
    private String namePart;
    private String descriptionPart;
    private List<String> sortParams;
    private Integer page;

    public GiftCertificateSpecification(List<String> tags, String namePart, String descriptionPart,
                                        List<String> sortParams, Integer page) {
        this.tags = tags;
        this.namePart = namePart;
        this.descriptionPart = descriptionPart;
        this.sortParams = sortParams;
        this.page = page;
    }

    @Override
    public TypedQuery<GiftCertificate> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        if (Stream.of(tags, namePart, descriptionPart, sortParams, page).allMatch(Objects::isNull)) {
            TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(0);
            query.setMaxResults(PAGE_SIZE);
            return query;
        }

//        processTags(criteriaBuilder, root);

        if (tags != null && tags.size() > 0) {
            createWhereConditionsIfNotExists();
            jpaTagParameters = new HashMap<>();

            Subquery<String> subQueryForTags = criteriaQuery.subquery(String.class);
            Root<Tag> subRoot = subQueryForTags.from(Tag.class);
            Join<Object, Object> subJoin = subRoot.join("giftCertificates");
            subQueryForTags.select(subRoot.get("name")).where(criteriaBuilder.equal(subJoin.get("id"), root.get("id")));

            for (int i = 0; i < tags.size(); i++) {
                String parameter = "tag" + i;
                jpaTagParameters.putIfAbsent(parameter, tags.get(i));
                ParameterExpression<String> jpaParameter = criteriaBuilder.parameter(String.class, parameter);
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(jpaParameter).value(subQueryForTags);
                whereConditions.add(inClause);
            }

//            ParameterExpression<String> tag1 = criteriaBuilder.parameter(String.class, "tag");
//            ParameterExpression<String> tag2 = criteriaBuilder.parameter(String.class, "tag2");
//
//            CriteriaBuilder.In<String> inTag1 = criteriaBuilder.in(tag1).value(subQueryForTags);
//            CriteriaBuilder.In<String> inTag2 = criteriaBuilder.in(tag2).value(subQueryForTags);
//
//            whereConditions.add(inTag1);
//            whereConditions.add(inTag2);
//              ##############################
//            createWhereConditionsIfNotExists();
//
//            Join<Object, Object> tagJoin = root.join("tags", JoinType.LEFT);
//            Path<Object> tagName = tagJoin.get("name");
//
//            CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(tagName);
//            inClause.value(tags.get(0));
//            inClause.value(tags.get(1));
//
//            whereConditions.add(inClause);
        }

        processNamePart(criteriaBuilder, root);
        processDescriptionPart(criteriaBuilder, root);
        processSortingParameters(criteriaBuilder, root);

        criteriaQuery.select(root);
        Predicate finalPredicate = receiveFinalPredicate(criteriaBuilder);
        criteriaQuery.where(finalPredicate);
        if (orderConditions != null) {
            criteriaQuery.orderBy(orderConditions);
        }
        criteriaQuery.distinct(true);

        if (page == null) {
            page = 1;
        }

        TypedQuery<GiftCertificate> targetQuery = entityManager.createQuery(criteriaQuery);
        targetQuery.setFirstResult(PAGE_SIZE * (page - 1));
        targetQuery.setMaxResults(PAGE_SIZE);

        if (jpaTagParameters != null && jpaTagParameters.size() > 0) {
            jpaTagParameters.forEach(targetQuery::setParameter);
        }

//        targetQuery.setParameter("tag0", tags.get(0));
//        targetQuery.setParameter("tag1", tags.get(1));

        return targetQuery;
    }

    private void processSortingParameters(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        if (sortParams != null && sortParams.size() > 0) {
            orderConditions = new ArrayList<>();

            sortParams.forEach(sortParam -> {
                String sortColumn = sortParam.substring(1);
                if (sortParam.startsWith("+")) {
                    orderConditions.add(criteriaBuilder.asc(root.get(sortColumn)));
                } else {
                    orderConditions.add(criteriaBuilder.desc(root.get(sortColumn)));
                }
            });
        }
    }

    private void processDescriptionPart(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        if (descriptionPart != null) {
            createWhereConditionsIfNotExists();

            Predicate namePredicate = criteriaBuilder.like(root.get("description"), "%" + descriptionPart + "%");
            whereConditions.add(namePredicate);
        }
    }

    private void processNamePart(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        if (namePart != null) {
            createWhereConditionsIfNotExists();

            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + namePart + "%");
            whereConditions.add(namePredicate);

        }
    }

    private void processTags(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        if (tags != null && tags.size() > 0) {
            createWhereConditionsIfNotExists();

            Join<Object, Object> tagJoin = root.join("tags", JoinType.LEFT);
            Path<Object> tagName = tagJoin.get("name");

            ParameterExpression<String> tag1 = criteriaBuilder.parameter(String.class, "tag1");
            new HashMap<String, ParameterExpression<String>>();

            CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(tagName);
            inClause.value(tags.get(0));
            inClause.value(tags.get(1));

            whereConditions.add(inClause);

//            tags.forEach(tag -> whereConditions.add(criteriaBuilder.equal(tagName, tag)));
        }
    }

    private Predicate receiveFinalPredicate(CriteriaBuilder criteriaBuilder) {
        Predicate finalPredicate = null;
        if (whereConditions != null && whereConditions.size() > 0) {
            if (whereConditions.size() == 1) {
                return whereConditions.get(0);
            }

            finalPredicate = criteriaBuilder.and(whereConditions.get(0), whereConditions.get(1));
            if (whereConditions.size() >= 3) {
                for (int i = 2; i < whereConditions.size(); i++) {
                    finalPredicate = criteriaBuilder.and(finalPredicate, whereConditions.get(i));
                }
            }
        }
        return finalPredicate;
    }

    private void createWhereConditionsIfNotExists() {
        this.whereConditions = Optional.ofNullable(whereConditions).orElseGet(ArrayList::new);
    }

}