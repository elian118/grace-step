package com.postelian.backend.domain.user.dto;

import com.postelian.backend.domain.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequestDto {
    private String name;
    private String email;
    private Role role;
    private int page = 1;
    private int size = 10;
    private String[] sort;
}
