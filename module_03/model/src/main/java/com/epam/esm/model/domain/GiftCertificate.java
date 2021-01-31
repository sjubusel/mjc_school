package com.epam.esm.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"tags", "orderPositions"})
@ToString(callSuper = true, exclude = {"tags", "orderPositions"})
@SuperBuilder(setterPrefix = "set")
public class GiftCertificate extends GeneralEntity<Long> {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    /**
     * a period of time when a gift certificate is available after its activation
     */
    @Column
    private Integer duration;

    @Column(name = "create_date", updatable = false, columnDefinition = "TIMESTAMP")
    private Instant createDate;

    @Column(name = "last_update_date", columnDefinition = "TIMESTAMP")
    private Instant updateDate;

    @ManyToMany
    @JoinTable(name = "join_certificates_tags_table",
            joinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags;

    @OneToMany(mappedBy = "giftCertificate")
    private Set<OrderPosition> orderPositions;
}
