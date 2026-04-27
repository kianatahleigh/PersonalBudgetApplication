/*This is the home controller. It redirects the root url to the login endpoint.*/

package edu.usca.rmoment.personalbudgetapplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}