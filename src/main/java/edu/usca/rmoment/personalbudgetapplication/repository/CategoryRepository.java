/* This is the repository interface for the category entities. It extends the
 * JpaRepository to provide built-in CRUD operations. It defines custom query methods
 * for retrieving a category by user ID, and accessing a category by ID. It acts as the
 * data access layer between teh application and database.*/


package edu.usca.rmoment.personalbudgetapplication.repository;

import edu.usca.rmoment.personalbudgetapplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category,Long>{

List<Category> findByUserId(long userid);

Category getCategoryById(long id);

Category getCategoryByUserId(long userid);

}
