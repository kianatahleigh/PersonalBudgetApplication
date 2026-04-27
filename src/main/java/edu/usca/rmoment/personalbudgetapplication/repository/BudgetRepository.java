/* This is the repository interface for the budget entities. It extends the
 * JpaRepository to provide built-in CRUD operations. It defines custom query methods
 * for retrieving a budget by user ID and associated user entity. It acts as the data
 * access layer between the application and database.*/

package edu.usca.rmoment.personalbudgetapplication.repository;



import edu.usca.rmoment.personalbudgetapplication.model.Budget;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget,Long> {
    Optional<Budget> findById(Long id);

    List<Budget> findByUserId(Long userId);

     List<Budget> findByUser(User user);





}
