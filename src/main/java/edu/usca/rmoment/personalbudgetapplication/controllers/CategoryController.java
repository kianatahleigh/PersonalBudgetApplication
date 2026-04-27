/*This is the controller for handling category related requests. It manages endpoints
* for creating, viewing, and updating categories as well as retrieving transactions
* associated with a specific category. It coordinates with CategoryService,
* UserService, and TransactionService to manage user specific category data and
* related transactions.*/




package edu.usca.rmoment.personalbudgetapplication.controllers;



import jakarta.validation.Valid;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.model.Type;
import edu.usca.rmoment.personalbudgetapplication.services.UserService;
import edu.usca.rmoment.personalbudgetapplication.services.TransactionService;
import edu.usca.rmoment.personalbudgetapplication.services.CategoryService;
import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;



    @GetMapping
    public String viewCategory(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "ViewCategories";
    }


    @GetMapping("/new")
    public String showCreateCategoryForm(Model model) {
        Category category = new Category();

        // this associates the new category with the currently authenticated user
        User user = userService.getAuthenticatedUser();
        category.setUser(user);


        model.addAttribute("category", category);
        model.addAttribute("user", user);
        return "CreateCategory";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            return "CreateCategory";
        }

        try {
            // always get fresh authenticated user
            User user = userService.getAuthenticatedUser();

            // attach it to the category before saving
            category.setUser(user);

            categoryService.saveCategory(category);

        } catch (Exception e) {
            System.err.println("Error saving category: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error saving category");
            return "CreateCategory"; // stay here, not login
        }

        return "redirect:/user/dashboard";
    }



    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "CreateCategory";
}


@PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") Category category){

        //makes sure updates are done under teh authenticated user
        User user = userService.getAuthenticatedUser();
        categoryService.saveCategory(category);
        return "redirect:/categories";

}


@GetMapping("/transactions/{categoryId}")
    public String categoryTransactions(@PathVariable("categoryId") long categoryId, Model model) {

        Category category = categoryService.getCategoryById(categoryId);

        if (category == null) {
            model.addAttribute("error", "Category not found.");
            return "errorPage";
        }

        //it retrieves all transactions associated with the category selected
        List<Transaction> transactions = transactionService.getTransactionsByCategory(category);

        model.addAttribute("transactions", transactions);
        model.addAttribute("category", category);

        return "ViewTransactions";

}

@PostMapping("/transactions/update-type")
    public String updateTransactionType(@RequestParam Long transactionId, @RequestParam Type type) {

       //this updates the transaction type, but must align with the category type per the prior logic setup
        transactionService.updateTransactionType(transactionId, type);
        return "redirect:/categories/transactions/";
}


@GetMapping("/list")
    public String listCategory(Model model){
        List<Category> categories = categoryService.getCategoriesByUserId(userService.getAuthenticatedUser().getId());
        model.addAttribute("categories", categories);
        return "ViewCategories";
}

    }





