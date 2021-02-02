package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
public abstract class GeneralEntityDto<ID, T extends GeneralEntityDto<ID, T>> extends RepresentationModel<T> {

    private ID id;
}
