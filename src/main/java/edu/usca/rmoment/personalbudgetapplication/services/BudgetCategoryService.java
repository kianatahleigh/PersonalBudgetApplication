/* this is the service layer for budget category entities. It handles operations such as creating, retrieving,
and deleting budget-category relationships. It acts as an intermediary between the controller layer and
budget category repository, delegating data access and managing associations between budgets and categories.*/


package edu.usca.rmoment.personalbudgetapplication.services;


import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.BudgetCategory;
import edu.usca.rmoment.personalbudgetapplication.repository.BudgetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetCategoryService {


    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;


    public BudgetCategory getBudgetCategoryById(Long id){
        return budgetCategoryRepository.findById(id).orElse(null);
    }

    public BudgetCategory save(BudgetCategory budgetCategory) {
        return budgetCategoryRepository.save(budgetCategory);
    }


    public void deleteBudgetCategory(long id) {
        budgetCategoryRepository.deleteById(id);
    }


}
