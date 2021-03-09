package com.epam.esm.repository.specification;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@NoArgsConstructor
@EqualsAndHashCode
public class GiftCertificateSpecification implements Specification<GiftCertificate> {

    private List<Predicate> whereConditions;
    private List<Order> orderConditions;
    private Map<String, String> jpaTagParameters;

    private List<String> tags;
    private String namePart;
    private String descriptionPart;
    private List<String> sortParams;

    public GiftCertificateSpecification(List<String> tags, String namePart, String descriptionPart,
                                        List<String> sortParams) {
        this.tags = tags;
        this.namePart = namePart;
        this.descriptionPart = descriptionPart;
        this.sortParams = sortParams;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (Stream.of(tags, namePart, descriptionPart, sortParams).allMatch(Objects::isNull)) {
            return null;
        }

        processTags(root, query, criteriaBuilder);
        processNamePart(root, criteriaBuilder);
        processDescriptionPart(root, criteriaBuilder);
        processSortingParameters(root, criteriaBuilder);

        Predicate finalPredicate = receiveFinalPredicate(root, criteriaBuilder);
        assembleCriteriaQuery(query, finalPredicate);

        return finalPredicate;
    }

    private void processTags(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (tags != null && tags.size() > 0) {
            createWhereConditionsIfNotExists();
            jpaTagParameters = new HashMap<>();

            Subquery<String> subQueryForTags = query.subquery(String.class);
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
        }
    }

    private void processNamePart(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder) {
        if (namePart != null) {
            createWhereConditionsIfNotExists();

            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + namePart + "%");
            whereConditions.add(namePredicate);
        }
    }

    private void processDescriptionPart(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder) {
        if (descriptionPart != null) {
            createWhereConditionsIfNotExists();

            Predicate namePredicate = criteriaBuilder.like(root.get("description"), "%" + descriptionPart + "%");
            whereConditions.add(namePredicate);
        }
    }

    private void processSortingParameters(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder) {
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

    private void createWhereConditionsIfNotExists() {
        this.whereConditions = Optional.ofNullable(whereConditions).orElseGet(ArrayList::new);
    }

    private Predicate receiveFinalPredicate(Root<GiftCertificate> root, CriteriaBuilder criteriaBuilder) {
        createWhereConditionsIfNotExists();
        whereConditions.add(criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE));

        if (whereConditions.size() == 1) {
            return whereConditions.get(0);
        }

        Predicate finalPredicate = criteriaBuilder.and(whereConditions.get(0), whereConditions.get(1));
        if (whereConditions.size() >= 3) {
            for (int i = 2; i < whereConditions.size(); i++) {
                finalPredicate = criteriaBuilder.and(finalPredicate, whereConditions.get(i));
            }
        }

        return finalPredicate;
    }

    private void assembleCriteriaQuery(CriteriaQuery<?> query, Predicate finalPredicate) {
        query.where(finalPredicate);
        if (orderConditions != null) {
            query.orderBy(orderConditions);
        }
        query.distinct(true);
    }
}
