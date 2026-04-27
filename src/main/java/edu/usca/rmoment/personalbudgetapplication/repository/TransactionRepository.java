/* This is the repository interface for the transaction entities. It extends the
* JpaRepository to provide built-in CRUD operations. It defines custom query methods
* for retrieving transactions by user ID, category, and category ID. It acts as the
* data access layer between the application and database.*/


package edu.usca.rmoment.personalbudgetapplication.repository;


import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.model.BudgetCategory;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Optional<Transaction> findById(Long id);

    List<Transaction> findByUserId(Long userId);


    List<Transaction> findByCategory(Category category);

    List<Transaction> findByCategoryId(Long categoryId);
}