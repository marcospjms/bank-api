package br.com.bank.util.tests;


import br.com.bank.model.Account;
import br.com.bank.model.auth.BankUser;
import br.com.bank.services.AccountService;
import br.com.bank.services.BankUserService;
import br.com.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UtilTestService {

    @Autowired
    private BankUserService bankUserService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    public BankUser adminUser;
    public BankUser customer;
    public BankUser customer2;

    public Account account;
    public Account account2;

    @PostConstruct
    public void setup() {
        this.bankUserService.deleteAll();
        this.transactionService.deleteAll();
        this.accountService.deleteAll();
        this.adminUser = this.bankUserService.createUser(
                BankUser.builder()
                        .name("Silva Admin Fulano")
                        .email("silvaadminfulano@google.com")
                        .username("silvaadminfulano")
                        .password("123456")
                        .build(),
                true
        );
        this.adminUser = this.bankUserService.findById(this.adminUser.getId()); // para ficar com o password na memória
        this.customer = this.bankUserService.createUser(
                BankUser.builder()
                        .name("Fulano da silva")
                        .email("fulanosilva@gmail.com")
                        .username("fulanosilva")
                        .password("123456")
                        .build(),
                false
        );
        this.customer2 = this.bankUserService.createUser(
                BankUser.builder()
                        .name("Sicrano da Silva")
                        .email("sicranosilva@gmail.com")
                        .username("sicranosilva")
                        .password("123456")
                        .build(),
                false
        );
        this.customer = this.bankUserService.findById(this.customer.getId());
        this.customer2 = this.bankUserService.findById(this.customer2.getId());

        this.account = this.accountService.createAccount(this.customer);
        this.account2 = this.accountService.createAccount(this.customer2);

        this.account.setBalance(10000d);
        this.account2.setBalance(10000d);

        this.accountService.save(this.account);
        this.accountService.save(this.account2);
    }
}