package com.bz.demo.service;


import com.bz.demo.facade.data.UserData;
import com.bz.demo.model.common.UserStatus;
import com.bz.demo.model.common.UserType;
import com.bz.demo.model.security.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(Long id);
    User getUser(Long id);
    User saveUser(User user);
    boolean isUsernameExists(String username);
    boolean isUserEmailExists(String email);

    void deleteUser(Long id);
    List<User> findAllUsers();
    User saveRoleAssignment(User user, List<Long> roleIds);
    boolean isUserCreated(Long objectRefId);
    List<UserData>userList(String userName, UserType userType, UserStatus userStatus);

}
