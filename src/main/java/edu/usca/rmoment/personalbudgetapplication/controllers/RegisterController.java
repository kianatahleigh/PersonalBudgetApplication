/*This is the controller for registration. It manages the endpoint for the registration
 form and processing user signup requests. It also performs validation such as password
 confirmation and handles and signup errors by returning necessary feedback to the user
 interface.*/


package edu.usca.rmoment.personalbudgetapplication.controllers;

import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "UserRegistration";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user,
                                     BindingResult bindingResult,
                                     Model model){
        if(bindingResult.hasErrors()){
            return "UserRegistration";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "UserRegistration";
        }

        try {
            userService.saveUser(user);
            return"redirect:/login";


        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "emailExist", e.getMessage());
            return "UserRegistration";


        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            return "UserRegistration";
        }

    }

}
