package com.bz.demo.security;


import com.bz.demo.facade.data.AuthenticatedUser;
import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;
import com.bz.demo.model.security.User;
import com.bz.demo.service.DocumentService;
import com.bz.demo.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @NonNull private final HttpSession httpSession;
    @NonNull private final UserService userService;
    @NonNull private final DocumentService documentService;

    @Override public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String imageUrl= "";
            String currentUserName = authentication.getName();
            AuthenticatedUser user = userService.findByUsername(currentUserName)
                    .map(thisUser -> new AuthenticatedUser(thisUser.getId(), thisUser.getUsername(), thisUser.getFullName(), thisUser.getRoles(), thisUser.getUserType()))
                    .orElseThrow(() -> new AccessDeniedException("Authenticated User not found"));
            imageUrl ="/images/user.png";
            User loginUser=userService.getUser(user.getId());
            Document document=documentService.findDocumentBySourceAndObject(DocumentSource.USER_PHOTO,user.getId()).orElse(null);
            if(Objects.nonNull(document)){
                imageUrl="";
                imageUrl=document.getImageUrl();
            }
            httpSession.setAttribute("imageUrl", imageUrl);
            httpSession.setAttribute("authenticatedUser", user);
            httpSession.setAttribute("designation", "");
            response.sendRedirect("/dashboard");
        }
    }

}
