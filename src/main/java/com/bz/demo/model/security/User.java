package com.bz.demo.model.security;


import com.bz.demo.model.Auditable;
import com.bz.demo.model.BaseEntity;
import com.bz.demo.model.common.UserStatus;
import com.bz.demo.model.common.UserType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder")
@Entity
@Table(name = "BG_USER")
public class User extends BaseEntity implements Auditable {

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "SECURITY_WORD")
    private String password;

    @Column(name = "USER_TYPE_ID")
    private UserType userType;

    @Column(name = "OBJECT_REF_ID")
    private Long objectRefId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "LOGIN_USER")
    private String fullName;

    @Column(name = "CONTACT_NO")
    private String contactNo;

    @Column(name = "EMAIL_ID")
    private String emailAddress;

    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ACTIVE_STATUS")
    private int activeStatus;

    @Column(name = "ENTERED_BY")
    private Long createdBy;

    @Column(name = "ENTRY_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Column(name = "UPDATE_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "USER_STATUS")
    private UserStatus userStatus;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "BG_USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.roles = user.roles;
    }

}
