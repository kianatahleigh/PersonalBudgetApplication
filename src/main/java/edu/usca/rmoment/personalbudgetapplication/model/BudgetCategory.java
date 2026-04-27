
/* represents a budget category in the system and maps "budgetcategories" table in the database.
It stores allocation amounts for a specific category within a budget. Each budget category is
associated with a budget and category.
 */


package edu.usca.rmoment.personalbudgetapplication.model;


import jakarta.persistence.*;
import lombok.Data;





@Entity
@Data
@Table(name = "budget_categories")
public class BudgetCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "allocated_amount")
    private Double allocatedAmount;


    @ManyToOne
    @JoinColumn(name = "budget", nullable = false)
    private  Budget budget;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;



}
