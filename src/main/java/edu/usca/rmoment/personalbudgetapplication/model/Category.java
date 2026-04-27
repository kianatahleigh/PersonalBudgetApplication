
/* represents a category in the system and stores information for categories.
It also maps "categories" table in the database, while also storing category-related
 information and enforces validation rules. Each category is associated with a user and a type
  ,in addition to transactions and budget categories.
 */


package edu.usca.rmoment.personalbudgetapplication.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
@Table(name = "categories")

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<BudgetCategory> budgetCategories;


}
