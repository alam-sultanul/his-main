package com.bz.demo.facade.impl;

/**
 * created by srana on 07/10/20 at 10.48 AM
 */

import com.bz.demo.facade.SessionManagementService;
import com.bz.demo.facade.data.AuthenticatedUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionManagementServiceImpl implements SessionManagementService {
    @NonNull private final HttpSession httpSession;

    @Override public AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) httpSession.getAttribute("authenticatedUser");
    }

}
