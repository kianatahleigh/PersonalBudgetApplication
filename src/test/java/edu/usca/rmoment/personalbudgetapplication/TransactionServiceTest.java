package edu.usca.rmoment.personalbudgetapplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.usca.rmoment.personalbudgetapplication.model.Transaction;
import edu.usca.rmoment.personalbudgetapplication.repository.TransactionRepository;
import edu.usca.rmoment.personalbudgetapplication.services.TransactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


//Adding a transaction
    @Test
    void testSaveTransaction() {

        Transaction t = new Transaction();
        t.setTitle("Groceries");
        t.setAmount(50.0);

        when(transactionRepository.save(t)).thenReturn(t);


        Transaction saved = transactionService.saveTransaction(t);


        assertNotNull(saved);
        assertEquals("Groceries", saved.getTitle());
        assertEquals(50.0, saved.getAmount(), 0.001);
    }


//Edit a Transaction
    @Test
    void testUpdateTransaction() {
        Transaction t = new Transaction();
        t.setTitle("Rent");
        t.setAmount(1200.0);

        when(transactionRepository.save(t)).thenReturn(t);

        Transaction saved = transactionService.updateTransaction(t);

        assertNotNull(saved);
        assertEquals("Rent", saved.getTitle());
        assertEquals(1200.0, saved.getAmount(), 0.001);

    }

//Delete a Transaction
    @Test
    void testDeleteTransaction() {
        long transactionId = 1L;

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).deleteById(transactionId);


    }





}