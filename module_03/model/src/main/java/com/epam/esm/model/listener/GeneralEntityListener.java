package com.epam.esm.model.listener;

import com.epam.esm.model.domain.GeneralEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Slf4j
public class GeneralEntityListener {

    @PrePersist
    void prePersist(GeneralEntity<Long> entity) {
        log.info("Attempting to add a new entity: {}", entity);
    }

    @PostPersist
    void postPersist(GeneralEntity<Long> entity) {
        log.info("Added an entity: {}", entity);
    }

    @PreUpdate
    void preUpdate(GeneralEntity<Long> entity) {
        log.info("Attempting to update an entity ({}) with ID: {}", entity.getClass().getName(), entity.getId());
    }

    @PostUpdate
    void postUpdate(GeneralEntity<Long> entity) {
        log.info("Updated an entity: {}", entity);
    }

    @PreRemove
    void preRemove(GeneralEntity<Long> entity) {
        log.info("Attempting to delete an entity ({}) with ID: {}", entity.getClass().getName(), entity.getId());
    }

    @PostRemove
    void postRemove(GeneralEntity<Long> entity) {
        log.info("Deleted an entity: {}", entity);
    }

    @PostLoad
    void postLoad(GeneralEntity<Long> entity) {
        log.info("Entity loaded: {}", entity);
    }

}
