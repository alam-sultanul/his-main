package com.bz.demo.controller;
import com.bz.demo.facade.SessionManagementService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class HomeController {
    @NonNull
    private final SessionManagementService sessionManagementService;
    @GetMapping({"/", "/dashboard"})

    public String dashboard(Model model, HttpServletRequest request) {
        log.error("Login error");
        log.info("Login info");
        log.warn("Login Completed");
        request.getSession().setAttribute("authenticatedUser", sessionManagementService.getAuthenticatedUser());
        return "/web/dashboard";
    }

    @GetMapping("/blank")
    public String home2() {
        return "/web/blank";
    }

    @GetMapping("/page")
    public String page() {
        return "/web/pages/page";
    }

    @GetMapping("/about")
    public String about() {
        return "/web/about";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @GetMapping("/404")
    public String error404() {
        return "/error/404";
    }
}
