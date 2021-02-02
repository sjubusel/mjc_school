package com.epam.esm.model.domain;

import com.epam.esm.model.entity_listener.GeneralEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "tags")
@EntityListeners(GeneralEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "giftCertificates")
@ToString(callSuper = true, exclude = "giftCertificates")
public class Tag extends GeneralEntity<Long> {

    @Column
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> giftCertificates;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "delete_date", columnDefinition = "TIMESTAMP")
    private Instant deleteDate;
}
