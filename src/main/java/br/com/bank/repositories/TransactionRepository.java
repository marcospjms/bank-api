package br.com.bank.repositories;

import br.com.bank.model.Transaction;
import br.com.bank.model.TransactionType;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>, QueryByExampleExecutor<Transaction> {

    @Query("SELECT transaction FROM Transaction transaction")
    List<Transaction> findAll(Pageable pageable);

    @Query("SELECT transaction FROM Transaction transaction WHERE transaction.type = :type " +
            "AND transaction.executedDate <= :data")
    List<Transaction> findByTypeAndDate(@Param("type") TransactionType transactionType, @Param("data") DateTime data);

    @Query("SELECT transaction FROM Transaction transaction WHERE transaction.executedDate <= :date " +
            " AND transaction.source.bankUser.id = :userId")
    List<Transaction> findByDateAndUser(Pageable pageable, @Param("date") DateTime date, @Param("userId") Long userId);
}
