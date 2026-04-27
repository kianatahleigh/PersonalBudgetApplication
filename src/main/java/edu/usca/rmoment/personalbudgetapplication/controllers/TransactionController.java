/*This is the controller for handling transaction related requests. It manages endpoints
* for creating, viewing, updating, and deleting transactions. In addition to supporting
* filtering and sorting transactions based on user-selected parameters. It coordinates
* with TransactionService, UserService, and CategoryService to retrieve and manage
* user-specific transaction data. It also includes validation and access control to ensure
* transactions are correctly associated with the authenticated user. */





package edu.usca.rmoment.personalbudgetapplication.controllers;

import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.model.Type;
import edu.usca.rmoment.personalbudgetapplication.repository.CategoryRepository;
import edu.usca.rmoment.personalbudgetapplication.repository.TransactionRepository;
import edu.usca.rmoment.personalbudgetapplication.repository.UserRepository;
import edu.usca.rmoment.personalbudgetapplication.services.CategoryService;
import edu.usca.rmoment.personalbudgetapplication.services.TransactionService;
import edu.usca.rmoment.personalbudgetapplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;




@GetMapping("/new")
    public String showCreateTransactionForm(Model model) {
    //Retrieves the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User loggedInUser = userService.getUserByEmail(email);

        List<Transaction> transactions = transactionService.getTransactionsByUserId(loggedInUser.getId());
        model.addAttribute("transactions", transactions);

    model.addAttribute("categories",
            categoryService.getCategoriesByUserId(loggedInUser.getId()));

    model.addAttribute("transaction", new Transaction());

        return "CreateTransaction";
    }




    @PostMapping("/create")
    public String createTransactionForm(@ModelAttribute @Valid Transaction transaction,
                                        @RequestParam Long categoryId,
                                        BindingResult result,
                                        Model model){

        if(result.hasErrors()){
            model.addAttribute("transactions",
                    transactionService.getTransactionsByUserId(transaction.getUser().getId()));
            model.addAttribute("categories", categoryService.getCategoriesByUserId(transaction.getUser().getId()));
            return "CreateTransaction";
        }

        // get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        transaction.setUser(user);

        // get category
        Category category = categoryService.getCategoryById(categoryId);
        transaction.setCategory(category);

        // it ensures transaction type matches the selected category type(income/expense)
        if (!category.getType().equals(transaction.getType())) {
            model.addAttribute("error", "Transaction type must match category type");
            model.addAttribute("categories", categoryService.getCategoriesByUserId(user.getId()));
            return "CreateTransaction";
        }
        // save
        transactionService.saveTransaction(transaction);

        return "redirect:/transactions/view";
    }



    @GetMapping("/edit/{id}")
    public String showEditTransactionForm(@PathVariable("id") long id, Model model) {
    Transaction transaction = transactionService.getTransactionById(id);
    model.addAttribute("transaction", transaction);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    User loggedInUser = userService.getUserByEmail(email);

    //this keeps users from changing transactions that do not belong to them
        if (transaction.getUser().getId() != loggedInUser.getId()) {
            return "redirect:/transactions";
        }

        List<Category> categories = categoryService.getCategoriesByUserId(loggedInUser.getId());
    model.addAttribute("categories", categories);


    return"TransactionEdit";
    }


// updates an existing transaction for the authenticated user
    @PostMapping("/edit/{id}")
    public String updateTransaction(@PathVariable Long id,
                                    @ModelAttribute Transaction transaction,
                                    @RequestParam Long categoryId,
                                    RedirectAttributes redirectAttributes) {

        // get existing transaction
        Transaction existingTransaction = transactionService.getTransactionById(id);

        // get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        existingTransaction.setUser(user);

        // gets category
        Category category = categoryService.getCategoryById(categoryId);
        existingTransaction.setCategory(category);

        // validation(makes sure transaction type matches category type)
        if (!category.getType().equals(transaction.getType())) {
            redirectAttributes.addFlashAttribute("error", "Transaction type must match category type");
            return "redirect:/transactions/edit/" + id;
        }

        //  updates only editable fields while preserving existing relationships
        existingTransaction.setTitle(transaction.getTitle());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setTransactionDate(transaction.getTransactionDate());
        existingTransaction.setType(transaction.getType());

        transactionService.saveTransaction(existingTransaction);

        redirectAttributes.addFlashAttribute("message", "Transaction updated successfully");
        return "redirect:/transactions/view";
    }



    @GetMapping("/delete/{id}")
    public String showDeleteTransactionForm(@PathVariable Long id, Model model){
        Transaction transaction = transactionService.getTransactionById(id);
        model.addAttribute("transaction", transaction);
        return"TransactionDelete";
    }


    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id){
        Transaction transaction = transactionService.getTransactionById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User loggedInUser = userService.getUserByEmail(email);

        if (transaction.getUser().getId() != loggedInUser.getId()) {

            return "redirect:/transactions";
        }

        transactionRepository.deleteById(id);
        return "redirect:/transactions/view";
    }

//Retrieve filtered and sorted transactions based on optional query parameters
    @GetMapping("/view")
    public String viewTransactions(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User loggedInUser = userService.getUserByEmail(email);

        List<Transaction> transactions = transactionService.filterTransactions(
                loggedInUser.getId(), categoryId, type, startDate, endDate, minAmount, maxAmount, sort, dir);

        model.addAttribute("transactions", transactions);
        model.addAttribute("categories",
                categoryService.getCategoriesByUserId(loggedInUser.getId()));

        return "ViewTransactions";


    }


}
