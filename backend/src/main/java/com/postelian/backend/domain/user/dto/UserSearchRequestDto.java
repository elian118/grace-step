package com.postelian.backend.domain.user.dto;

import com.postelian.backend.domain.user.entity.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class UserSearchRequestDto {
    private String name;
    private String email;
    private Role role;
    private Pageable pageable;
}
