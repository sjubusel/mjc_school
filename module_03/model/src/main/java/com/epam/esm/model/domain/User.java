package com.epam.esm.model.domain;

import com.epam.esm.model.listener.GeneralEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
@EntityListeners(GeneralEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "orders")
@ToString(callSuper = true, exclude = "orders")
public class User extends GeneralEntity<Long> {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;
}
