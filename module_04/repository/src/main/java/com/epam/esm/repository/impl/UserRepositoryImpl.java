package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GeneralCrudRepository<User, Long> implements UserRepository {

    protected UserRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = entityManager.createQuery("SELECT user FROM User AS user WHERE user.login=:login",
                User.class).setParameter("login", username).getResultList();

        return !users.isEmpty() ? Optional.ofNullable(users.get(0)): Optional.empty();
    }

    @Override
    protected CriteriaQuery<User> getCriteriaQueryReadById(Long idToFind) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idToFind));
        criteriaQuery.select(root);

        return criteriaQuery;
    }

    @Override
    protected CriteriaQuery<User> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        return null;
    }

    @Override
    protected Query getDeleteQuery(Long aLong) {
        return null;
    }
}
