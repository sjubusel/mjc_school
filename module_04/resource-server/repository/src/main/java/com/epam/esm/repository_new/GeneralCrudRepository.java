package com.epam.esm.repository_new;

import com.epam.esm.model.domain.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GeneralCrudRepository<T extends Entity<ID>, ID extends Serializable> extends JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {

}
