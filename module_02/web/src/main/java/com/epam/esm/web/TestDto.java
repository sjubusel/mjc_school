package com.epam.esm.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {

    @Positive
    private Long id;

    @Pattern(regexp = "[A-Za-zА-Яа-яЁё]{1,256}")
    private String name;

    @Null
    private String secret;
}
