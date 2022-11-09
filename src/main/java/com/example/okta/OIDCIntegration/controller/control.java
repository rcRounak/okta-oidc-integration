package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}
