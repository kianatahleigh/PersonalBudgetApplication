/*This is the user controller , and it handles user-related requests and dashboard
* functionality. It controls the endpoints for displaying the user profile, rendering
* the user dashboard, user creation. It coordinates with the UserService, TransactionService,
* and BudgetService to retrieve user-specific data. It performs dashboard related calculations
* for the financial summary such as total income, total expenses, balance, and spending analytics
* for display on the frontend.*/






package edu.usca.rmoment.personalbudgetapplication.controllers;


import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.model.Type;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.services.BudgetService;
import edu.usca.rmoment.personalbudgetapplication.services.TransactionService;
import edu.usca.rmoment.personalbudgetapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;



import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

@Autowired
    private UserService userService;

@Autowired
private TransactionService transactionService;

@Autowired
private BudgetService budgetService;


@GetMapping("/profile")
    public String userProfile(Principal principal, Model model) {
Optional<User> user = userService.findByEmail(principal.getName());

if (user.isPresent()) {
    model.addAttribute("user", user.get());
} else {
    model.addAttribute("user", null);
}
return "UserProfile";

}


// I did not have an official dashboard controller so that is why all the logic is here
@GetMapping("/dashboard")
    public String userDashboard(Principal principal, Model model) {
    Optional<User> userOpt = userService.findByEmail(principal.getName());

    if (userOpt.isPresent()){

        User user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("budgets", user.getBudgets());

        Map<String, Double> preview =
                transactionService.getTopSpendingCategory(user);

        model.addAttribute("analyticsPreview", preview);



            // gets the transactions
            List<Transaction> transactions =
                    transactionService.getTransactionsByUserId(user.getId());


            // calculations
        double totalIncome = transactions.stream()
                .filter(t -> Type.INCOME.equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(t -> Type.EXPENSE.equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalIncome - totalExpenses;

       /* System.out.println("Income: " + totalIncome);
        System.out.println("Expenses: " + totalExpenses);
        System.out.println("Balance: " + balance); */

            // sends to the frontend
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("totalExpenses", Math.abs(totalExpenses));
            model.addAttribute("balance", balance);

        } else {

            model.addAttribute("user", null);
            model.addAttribute("budgets", new ArrayList<>());

            // default values
            model.addAttribute("totalIncome", 0);
            model.addAttribute("totalExpenses", 0);
            model.addAttribute("balance", 0);
        }

        return "dashboard";
    }

    @GetMapping("/user/dashboard")
    public String dashboard(Model model) {
        User user = userService.getAuthenticatedUser();

        List<Budget> budgets = budgetService.getBudgetsByUserId(user.getId());

        model.addAttribute("budgets", budgets);
        model.addAttribute("user", user);

        return "dashboard";
    }

@PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
    userService.saveUser(user);

    return "redirect:/user/dashboard";
}


@GetMapping
    public String logout(Model model){
    return "UserLogin";
}

}
