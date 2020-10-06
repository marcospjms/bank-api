package br.com.bank.repositories;

import br.com.bank.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>, QueryByExampleExecutor<Account> {

    @Query("SELECT account FROM Account account")
    List<Account> findAll(Pageable pageable);
}
