/* this is the service layer for budget. It handles operations such as creating and retrieving a budget.
That includes getting budgets associated with a specific user. It acts as an intermediary between the
controller layer and budget repository, delegating data access while encapsulating basic business logic.*/

package edu.usca.rmoment.personalbudgetapplication.services;


import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BudgetService {



    @Autowired
    private BudgetRepository budgetRepository;


    public Budget getBudgetById(long id) {

        return budgetRepository.findById(id).get();
    }

    public List<Budget> getBudgetsByUserId(long id) {

        return budgetRepository.findByUserId(id);
    }

    public Budget saveBudget(Budget budget) {

        return budgetRepository.save(budget);
    }


}
