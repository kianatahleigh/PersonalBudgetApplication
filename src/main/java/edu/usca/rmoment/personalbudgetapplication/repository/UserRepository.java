/* This is the repository interface for the user entities. It extends the
 * JpaRepository to provide built-in CRUD operations. It defines custom query methods
 * for retrieving a user by email. It acts as the data access layer between the
 * application and database.*/


package edu.usca.rmoment.personalbudgetapplication.repository;

import edu.usca.rmoment.personalbudgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);


}
