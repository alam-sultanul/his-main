package com.bz.demo.facade.data;

import com.bz.demo.model.common.UserStatus;
import com.bz.demo.model.common.UserType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserData {
    private Long id;
    private String fullName;
    private String userName;
    private UserType userType;
    private UserStatus userStatus;
    private int activeStatus;

    public UserData(Long id,String fullName,String userName,UserType userType,UserStatus userStatus){
        this.id=id;
        this.fullName=fullName;
        this.userName=userName;
        this.userType=userType;
        this.userStatus=userStatus;
    }
}
