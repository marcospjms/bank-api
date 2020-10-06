package br.com.bank.services;

import br.com.bank.model.Account;
import br.com.bank.model.auth.BankUser;
import br.com.bank.repositories.AccountRepository;
import br.com.bank.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account save(Account account) {
        return this.repository.save(account);
    }

    public Account createAccount(BankUser bankUser) {
        Account account = new Account();
        account.setBankUser(bankUser);
        account = this.repository.save(account);
        account.setCode(String.format("|%015d|", account.getId()));
        return this.repository.save(account);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<Account> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Account> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new Account(), query), pageable).getContent();
    }


    public Account findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }
}
