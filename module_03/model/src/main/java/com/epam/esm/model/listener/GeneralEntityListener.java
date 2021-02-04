package com.epam.esm.model.listener;

import com.epam.esm.model.domain.Entity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Slf4j
public class GeneralEntityListener {

    @PreUpdate
    void preUpdate(Entity<Long> entity) {
        log.info("Attempting to update an entity ({}) with ID: {}", entity.getClass().getName(), entity.getId());
    }

    @PostUpdate
    void postUpdate(Entity<Long> entity) {
        log.info("Updated an entity: {}", entity);
    }

    @PreRemove
    void preRemove(Entity<Long> entity) {
        log.info("Attempting to delete an entity ({}) with ID: {}", entity.getClass().getName(), entity.getId());
    }

    @PostRemove
    void postRemove(Entity<Long> entity) {
        log.info("Deleted an entity: {}", entity);
    }

}
