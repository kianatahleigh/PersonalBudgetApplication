/*This controller is for the login. It maps the endpoint for the login to the login view.
The authentication is handled by yhe spring security configuration. */



package edu.usca.rmoment.personalbudgetapplication.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {


    @GetMapping("/login")
    public String loginPage() {
        return "UserLogin";
    }


}

