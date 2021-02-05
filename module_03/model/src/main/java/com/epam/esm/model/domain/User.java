package com.epam.esm.model.domain;

import com.epam.esm.model.listener.GeneralEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@EntityListeners(GeneralEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends com.epam.esm.model.domain.Entity<Long> {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
