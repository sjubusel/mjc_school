package com.epam.esm.model.domain;

import com.epam.esm.model.listener.GeneralEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "certificates")
@EntityListeners(GeneralEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"tags", "orderPositions"})
@ToString(callSuper = true, exclude = {"tags", "orderPositions"})
public class GiftCertificate extends GeneralEntity<Long> {

    private String name;

    private String description;

    private BigDecimal price;

    /**
     * a period of time when a gift certificate is available after its activation
     */
    private Integer duration;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private Instant createDate;

    @Column(name = "last_update_date", columnDefinition = "TIMESTAMP")
    private Instant updateDate;

    @ManyToMany
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags;

    @OneToMany(mappedBy = "giftCertificate")
    private Set<OrderPosition> orderPositions;

    private Boolean isDeleted;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant deleteDate;
}
