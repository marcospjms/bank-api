package br.com.bank.services;

import br.com.bank.model.Account;
import br.com.bank.model.Transaction;
import br.com.bank.model.TransactionType;
import br.com.bank.model.auth.BankUser;
import br.com.bank.repositories.TransactionRepository;
import br.com.bank.util.AbstractEntityUtil;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository repository,  AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    public Transaction save(Transaction transaction) {
        return this.repository.save(transaction);
    }

    public synchronized Transaction execute(BankUser loggedUser, Transaction transaction) {
        switch (transaction.getType()) {
            case DEPOSIT:
                this.deposit(loggedUser, transaction);
                break;
            case TRANSFER:
                this.transfer(loggedUser, transaction);
                break;
            case WITHDRAWALS:
                this.withdrawals(loggedUser, transaction);
                break;
            default:
                throw new RuntimeException("Tipo de transação não reconhecido.");
        }

        return this.repository.save(transaction);
    }

    private void deposit(BankUser loggedUser, Transaction transaction) {
        if (!transaction.canDeposit(loggedUser)) {
            throw new RuntimeException("Depósito inválido");
        }
        transaction.setSource(null);
        Account target = accountService.findById(transaction.getTarget().getId());
        target.increaseBalance(transaction.getValue());
        transaction.setExecutedDate(DateTime.now());
        this.save(transaction);
        this.accountService.save(target);
    }

    private void transfer(BankUser loggedUser, Transaction transaction) {
        if (!transaction.canTransfer(loggedUser)) {
            throw new RuntimeException("Transferência inválida");
        }
        Account source = accountService.findById(transaction.getSource().getId());
        Account target = accountService.findById(transaction.getTarget().getId());
        source.decreaseBalance(transaction.getValue());
        target.increaseBalance(transaction.getValue());
        transaction.setExecutedDate(DateTime.now());
        this.save(transaction);
        this.accountService.save(source);
        this.accountService.save(target);
    }

    private void withdrawals(BankUser loggedUser, Transaction transaction) {
        if (!transaction.canWithdrawals(loggedUser)) {
            throw new RuntimeException("Saque inválido");
        }
        transaction.setTarget(null);
        Account source = accountService.findById(transaction.getSource().getId());
        source.decreaseBalance(transaction.getValue());
        transaction.setExecutedDate(DateTime.now());
        this.save(transaction);
        this.accountService.save(source);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<Transaction> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Transaction> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new Transaction(), query), pageable).getContent();
    }

    public Transaction findByIdAndBankUser(Long id, BankUser user) {
        return repository.findById(id).get();
    }

    public List<Transaction> findByTypeAndDate(TransactionType type) {
        return this.repository.findByTypeAndDate(type, DateTime.now());
    }

    public List<Transaction> findByDateAndUser(Pageable pageable, DateTime date, BankUser user) {
        return this.repository.findByDateAndUser(pageable, date, user.getId());
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }
}
