/*This is controller for budget related request. It manages endpoints for creating and viewing budgets.
* It also supports assigning category based allocations to a budget. Furthermore, it coordinates with
* BudgetService, CategoryService, UserService, and BudgetCategoryService to manage user specific
* budget data and associated category allocations. It processes allocation data from the frontend in
* JSON format.*/




package edu.usca.rmoment.personalbudgetapplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.BudgetCategory;
import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.repository.UserRepository;
import edu.usca.rmoment.personalbudgetapplication.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BudgetCategoryService budgetCategoryService;




@GetMapping("/new")
    public String showCreateBudgetForm(Model model) {
    Budget budget = new Budget();

        User user = userService.getAuthenticatedUser();
                budget.setUser(user);


        List<Category> categories = categoryService.getCategoriesByUserId(user.getId());
        model.addAttribute("budget", budget);

        model.addAttribute("categories", categories);
        model.addAttribute("user",budget.getUser());
        return "CreateBudget";
    }

    @PostMapping("/create")
    public String createBudget(
            @ModelAttribute Budget budget,
            @RequestParam("allocationsJson") String allocationsJson
    ) {

        User user = userService.getAuthenticatedUser();
        budget.setUser(user);

        Budget savedBudget = budgetService.saveBudget(budget);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            //parse JSON string containing category allocations from the frontend
            List<Map<String, Object>> allocations =
                    objectMapper.readValue(allocationsJson, List.class);

            //Create and save budgetcategory entries linking the budget, category, and allocated amount
            for (Map<String, Object> item : allocations) {

                //extract category ID and allocated amount from parse JSON
                Long categoryId = Long.valueOf(item.get("categoryId").toString());
                Double amount = Double.valueOf(item.get("amount").toString());

                Category category = categoryService.getCategoryById(categoryId);

                //Link budget and category with allocated amount
                BudgetCategory bc = new BudgetCategory();
                bc.setBudget(savedBudget);
                bc.setCategory(category);
                bc.setAllocatedAmount(amount);

                budgetCategoryService.save(bc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/user/dashboard";
    }




    @GetMapping("/{id}")
    public String viewBudget(@PathVariable Long id, Model model) {
        Budget budget = budgetService.getBudgetById(id);

        model.addAttribute("budget", budget);

        //retrieve categories associated with the budget's user to display
        model.addAttribute("categories",
                categoryService.getCategoriesByUserId(budget.getUser().getId()));

        return "ViewBudgets";
    }

}

