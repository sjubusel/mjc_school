package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends GeneralEntityDto<Long, UserDto> {

    @NotBlank(message = "first name must not be blank")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё \\-]{1,256}", message = "first name should contain from 1 to 256 characters")
    private String firstName;

    @NotBlank(message = "last name must not be blank")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё \\-]{1,256}", message = "last name should contain from 1 to 256 characters")
    private String lastName;

    @NotNull(message = "email must be filled")
    @Email(message = "email should be valid. Example, xxxxxxx@xxxxx.domain")
    private String email;

    @NotBlank(message = "phone number must not be empty")
    @Pattern(regexp = "[+][0-9]{1,3}[ ][(][0-9]{2,3}[)][ ][1-9][0-9]{2}[-][0-9]{2}[-][0-9]{2}",
            message = "phone number should be of the following format: " +
                    "+{country code} ({inner country code}) XXX-XX-XX")
    private String phoneNumber;

    @Null(message = "orders cannot be defined by users")
    private Set<OrderDto> orders;
}
