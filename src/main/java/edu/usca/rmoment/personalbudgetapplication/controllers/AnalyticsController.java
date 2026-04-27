/*This is the controller for handling analytic related requests. It gets the authenticated
* user's transaction data and aggregates spending by category for display in the analytics
* view. It also coordinates with UserService and TransactionService to provide user specific
* financial data/insights.*/



package edu.usca.rmoment.personalbudgetapplication.controllers;


import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.services.TransactionService;
import edu.usca.rmoment.personalbudgetapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping
public class AnalyticsController {


    @Autowired
    private UserService userService;


    @Autowired
    private TransactionService transactionService;



@GetMapping("/analytics")
    public String showAnalyticsForm(Model model, Principal principal) {

    User user = userService.getAuthenticatedUser();

    //it calculates total spending grouped by category for analytics to display
    Map<String, Double> spendingByCategory =
            transactionService.getSpendingByCategory(user);

     model.addAttribute("categorySpending", spendingByCategory);
     model.addAttribute("user", user);

     return "Analytics";


}



}
