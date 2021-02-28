package com.epam.esm.model.domain;

import com.epam.esm.model.other.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAuthority extends com.epam.esm.model.domain.Entity<Long> {

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
