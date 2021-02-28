package com.epam.esm.model.dto;

import com.epam.esm.model.other.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAuthorityDto extends EntityDto<Long, UserAuthorityDto> {

    private Role role;
}
