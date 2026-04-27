/*This is the controller for budgetcategory entities. It handles creating, updating, retrieving,
* and deleting relationships between budgets and categories, including the allocation amounts.
* It coordinates with the BudgetCategoryService, BudgetService, and CategoryService to manage
* category allocations within a specific budget.*/




package edu.usca.rmoment.personalbudgetapplication.controllers;


import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.BudgetCategory;
import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.services.BudgetCategoryService;
import edu.usca.rmoment.personalbudgetapplication.services.BudgetService;
import edu.usca.rmoment.personalbudgetapplication.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/budgetcategories")
public class BudgetCategoryController {

    @Autowired
    private BudgetCategoryService budgetCategoryService;

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        //loads existing budgetcategory relationship for editing
        BudgetCategory bc = budgetCategoryService.getBudgetCategoryById(id);

        model.addAttribute("budgetCategory", bc);

        model.addAttribute("categories", categoryService.getAllCategories());

        return "BudgetEdit";
    }




    @GetMapping("/delete/{id}")
    public String deleteBudgetCategory(@PathVariable Long id) {

        BudgetCategory bc = budgetCategoryService.getBudgetCategoryById(id);

        Long budgetId = bc.getBudget().getId();

        budgetCategoryService.deleteBudgetCategory(id);

        return "redirect:/budgets/" + budgetId;
    }



    @PostMapping("/update")
    public String updateBudgetCategory(
            @RequestParam Long id,
            @RequestParam Long categoryId,
            @RequestParam Double allocatedAmount) {

        BudgetCategory bc = budgetCategoryService.getBudgetCategoryById(id);

        //updates allocation amount and associated category for this budget entry
        bc.setAllocatedAmount(allocatedAmount);

        bc.setCategory(categoryService.getCategoryById(categoryId));

        budgetCategoryService.save(bc);

        Long budgetId = bc.getBudget().getId();

        return "redirect:/budgets/" + budgetId;
    }



    @PostMapping("/add")
    public String addBudgetCategory(
            @RequestParam Long budgetId,
            @RequestParam Long categoryId,
            @RequestParam Double allocatedAmount) {

        Budget budget = budgetService.getBudgetById(budgetId);
        Category category = categoryService.getCategoryById(categoryId);

        //creates a new association between budget and category with allocated amount
        BudgetCategory bc = new BudgetCategory();
        bc.setBudget(budget);
        bc.setCategory(category);
        bc.setAllocatedAmount(allocatedAmount);

        budgetCategoryService.save(bc);

        return "redirect:/budgets/" + budgetId;
    }


}
