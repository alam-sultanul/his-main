package com.bz.demo.facade;


import com.bz.demo.facade.data.AuthenticatedUser;

public interface SessionManagementService {
    AuthenticatedUser getAuthenticatedUser();
}
