package com.epam.esm.repository.aop;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.repository.specification.GiftCertificateSpecification;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * OAP is applied because Criteria API doesn't provide possibility to set named parameters before creation of TypedQuery
 * (see line 43)
 *
 * More information on "https://github.com/spring-projects/spring-data-jpa/issues/1231"
 */
@Aspect
@Component
public class GiftCertificateRepositoryAspect {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final EntityManager entityManager;
    private final SimpleJpaRepository<GiftCertificate, Long> jpaRepository;

    public GiftCertificateRepositoryAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaRepository = new SimpleJpaRepository<GiftCertificate, Long>(GiftCertificate.class, entityManager) {
            /*
            an anonymous class is used because inheritance of SimpleJpaRepository will cause ambiguity of beans
             */

            @Override
            public Page<GiftCertificate> findAll(Specification<GiftCertificate> spec, Pageable pageable) {
                GiftCertificateSpecification certificateSpecification = (GiftCertificateSpecification) spec;
                TypedQuery<GiftCertificate> query = getQuery(spec, pageable);

                applyBindingParametersIfExist(certificateSpecification, query);

                return isUnpaged(pageable) ? new PageImpl<>(query.getResultList())
                        : readPage(query, getDomainClass(), pageable, spec);
            }

            private boolean isUnpaged(Pageable pageable) {
                return pageable.isUnpaged();
            }

            private void applyBindingParametersIfExist(GiftCertificateSpecification certificateSpecification,
                                                       TypedQuery<GiftCertificate> query) {
                List<String> tags = certificateSpecification.getTags();
                if (tags != null && tags.size() > 0) {
                    for (int i = 0; i < tags.size(); i++) {
                        query.setParameter("tag" + i, tags.get(i));
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Around("findAllMethod()")
    public Object findAllInCustomMode(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length != 2) {
            return pjp.proceed();
        }

        Object specification = args[0];
        Object pageable = args[1];

        Specification<GiftCertificate> specification1 = (Specification<GiftCertificate>) specification;
        Pageable pageable1 = (Pageable) pageable;
        return jpaRepository.findAll(specification1, pageable1);
    }

    @Pointcut("execution(* com.epam.esm.repository.impl.GiftCertificateRepository.findAll(..))")
    public void findAllMethod() {
    }
}
