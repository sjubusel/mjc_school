package com.epam.esm.repository_new;

import com.epam.esm.model.domain.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GeneralCrudRepository<T extends Entity<ID>, ID extends Serializable> extends JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {

    @Modifying
    @Query("UPDATE #{#entityName} AS e SET e.isDeleted=true, e.deleteDate=CURRENT_TIMESTAMP WHERE e.id=?1")
    boolean deleteInSoftMode(ID id);
}
