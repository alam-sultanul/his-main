package com.bz.demo.repository;


import com.bz.demo.facade.data.UserData;
import com.bz.demo.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Long countByUsername(String username);
    Long countByEmailAddress(String emailAddress);
    Long countByObjectRefId(Long objectRefId);

    @Query("SELECT new com.bz.demo.facade.data.UserData(u.id,u.fullName,u.username,u.userType,u.userStatus)" +
            "FROM User u " +
            " where u.username LIKE CONCAT('%',:userName, '%') and u.userType =coalesce(:userType,u.userType) and u.userStatus =coalesce(:userStatus,u.userStatus)")
    List<UserData> findUsers(@Param("userName")String userName, @Param("userType") Integer userType, @Param("userStatus") Integer userStatus);
}
