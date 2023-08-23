package example.bank.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import example.bank.model.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transaction WHERE debit_credit_status = 'D' AND account_id = ?1 GROUP BY account_id, transaction.id;", nativeQuery = true)
    List<Transaction> selectDebitOnly(Integer id);

    @Query(value = "SELECT * FROM transaction WHERE account_id = ?1 AND transaction_date >= ?2 AND transaction_date <= ?3 GROUP BY account_id, transaction.id;", nativeQuery = true)
    List<Transaction> selectTransaction(Integer id, Date startDate, Date endDate);

    @Query(value = "SELECT t FROM Transaction t WHERE t.account.accountId = ?1 AND t.transactionDate >= ?2 AND t.transactionDate <= ?3")
    List<Transaction> selectTransactionNonNative(Integer id, Date startDate, Date endDate);
}
