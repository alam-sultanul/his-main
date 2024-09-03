package com.bz.demo.controller.signUp;
import com.bz.demo.model.common.UserStatus;
import com.bz.demo.model.common.UserType;
import com.bz.demo.model.security.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@NoArgsConstructor
public class SignUpForm {
    private Long id;
    private String fullName;
    private String userName;
    private String password;
    private UserType userType;
    private String email;
    private String contactNo;
    private String studentId;
    private Long objectRefId;
    private Long objectTypeId;
    private UserStatus userStatus;
    public boolean isPersisted() {
        return id != null;
    }

    public SignUpForm(User user){
        populateUserInfo(user);
    }

    private void populateUserInfo(User user){
        this.id=user.getId();
        this.fullName=user.getFullName();
        this.userName=user.getUsername();
        this.userType=user.getUserType();
        this.email=user.getEmailAddress();
        this.contactNo=user.getContactNo();
        this.userStatus=user.getUserStatus();
    }
}
