/* This is the repository interface for the budget category entities. It extends the
 * JpaRepository to provide built-in CRUD operations. It acts as the data access layer
 * between teh application and database.*/


package edu.usca.rmoment.personalbudgetapplication.repository;



import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory,Long> {

    Optional<BudgetCategory> findById(Long id);





}
