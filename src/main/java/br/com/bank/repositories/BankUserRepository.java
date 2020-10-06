package br.com.bank.repositories;

import br.com.bank.model.auth.BankUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankUserRepository extends CrudRepository<BankUser, Long>, QueryByExampleExecutor<BankUser> {

    @Query("SELECT bankUser FROM BankUser bankUser")
    List<BankUser> findAll(Pageable pageable);

    BankUser findByUsername(String username);
}
