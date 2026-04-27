/* this is the service layer for Transactions. It handles the core operations such as
* creating, updating, retrieving, and deleting transactions. It includes the logic for
* filtering transactions by category, type, amount, and date range. In addition to that,
* it also includes sorting results dynamically based on user input and also aggregates
* transaction data for analytics. It coordinates with the CategoryService and UserService
* to retrieve user-specific transaction data.*/

package edu.usca.rmoment.personalbudgetapplication.services;

import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.model.Type;
import edu.usca.rmoment.personalbudgetapplication.model.User;
import edu.usca.rmoment.personalbudgetapplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;



    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }


    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    public void deleteTransaction(long id) {
        transactionRepository.deleteById(id);
    }

    public void saveAllTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }


    public void updateTransactionType(Long transactionId, Type type) {
        Transaction transaction = getTransactionById(transactionId);
        transaction.setType(type);
        transactionRepository.save(transaction);
    }


    public Transaction updateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return transaction;
    }


    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactionRepository.findByCategory(category);
    }

    public List<Transaction> getTransactionsByCategoryId(Long categoryId) {
        return transactionRepository.findByCategoryId(categoryId);
    }


    public List<Transaction> getTransactionsByUserId(long userId) {
        List<Category> categories = categoryService.getCategoriesByUserId(userId);
        List<Transaction> transactions = new ArrayList<>();
        for (Category category : categories) {
            transactions.addAll(getTransactionsByCategoryId(category.getId()));
        }
        return transactions;
    }


    public List<Transaction> filterTransactions(Long userId,
                                                Long categoryId,
                                                Type type,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                Double minAmount,
                                                Double maxAmount,
                                                String sort,
                                                String dir){

      

        Stream<Transaction> stream = getTransactionsByUserId(userId).stream()

                .filter(t -> categoryId == null || t.getCategory().getId().equals(categoryId))
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> startDate == null || !t.getTransactionDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getTransactionDate().isAfter(endDate))
                .filter(t -> (minAmount == null || t.getAmount() >= minAmount) && (maxAmount == null || t.getAmount() <= maxAmount));

        /* default behavior*/
        if (sort == null) {
            return stream
                    .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                    .toList();
        }

        /* user-selected sorting*/
        Comparator<Transaction> comparator;

        switch (sort) {
            case "amount":
                comparator = Comparator.comparing(Transaction::getAmount);
                break;
            case "date":
                comparator = Comparator.comparing(Transaction::getTransactionDate);
                break;
            default:
                comparator = Comparator.comparing(Transaction::getTransactionDate);
        }

        if ("desc".equals(dir)) {
            comparator = comparator.reversed();
        }

        return stream.sorted(comparator).toList();
    }



  public Map<String, Double> getSpendingByCategory(User user){

      List<Transaction> transactions = getTransactionsByUserId(user.getId());

        Map<String, Double> categoryTotals = new HashMap<>();


        for (Transaction transaction : transactions) {

            Category category = transaction.getCategory();

            if (category == null) continue;

            String categoryName = transaction.getCategory().getName();
            double amount  = transaction.getAmount();


            categoryTotals.put(categoryName, categoryTotals.getOrDefault(categoryName, 0.0) + amount);

        }

        return categoryTotals;
  }



    public Map<String, Double> getTopSpendingCategory(User user) {

        Map<String, Double> totals = getSpendingByCategory(user);

        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .collect(
                        LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        Map::putAll
                );
    }

}
