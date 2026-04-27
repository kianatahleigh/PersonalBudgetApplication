package edu.usca.rmoment.personalbudgetapplication;

import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.model.Type;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class SummaryCalculationsTest {

//Summary Calculations

    @Test
    void testSummaryCalculations() {
        List<Transaction> transactions = new ArrayList<>();


        Transaction t1 = new Transaction();
        t1.setType(Type.INCOME);
        t1.setAmount(3500.00);

        Transaction t2 = new Transaction();
        t2.setType(Type.EXPENSE);
        t2.setAmount(350.00);


        Transaction t3 = new Transaction();
        t3.setType(Type.EXPENSE);
        t3.setAmount(50.00);

        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);

        double totalIncome = transactions.stream()
                .filter(t -> Type.INCOME.equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(t -> Type.EXPENSE.equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalIncome - totalExpenses;


        assertEquals(3500.00, totalIncome);
        assertEquals(400.00, totalExpenses);
        assertEquals(3100.00, balance);
    }
}
