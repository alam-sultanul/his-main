package com.bz.demo.facade.data;

import com.bz.demo.model.common.UserType;
import com.bz.demo.model.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {
    private Long id;
    private String username;
    private String fullName;
    private Set<Role> roleSet;
    private UserType userType;

    public boolean isAdmin() {
        return userType.equals(UserType.ADMIN);
    }

}
