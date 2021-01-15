package com.epam.esm.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
public class GiftCertificate extends Entity<Long> {

    private String name;
    private String description;
    private BigDecimal price;
    /**
     * a period of time when a gift certificate is available after its activation
     */
    private Integer duration;
    private Instant createDate;
    private Instant updateDate;
    private List<Tag> tags = new ArrayList<>();
}
