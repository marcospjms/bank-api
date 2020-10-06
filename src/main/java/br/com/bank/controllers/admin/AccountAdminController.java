package br.com.bank.controllers.admin;

import br.com.bank.model.Account;
import br.com.bank.model.auth.BankUser;
import br.com.bank.services.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/accounts")
public class AccountAdminController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "")
    public ResponseEntity<Account> save(@RequestBody BankUser user) {
        user.setPassword(null);
        return new ResponseEntity<>(this.accountService.createAccount(user), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Account> update(@RequestBody Account account) {
        return new ResponseEntity<>(this.accountService.save(account), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.accountService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Account> get(@PathVariable(value = "id") Long id) {
        Account account = this.accountService.findById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Account>> listAll(Pageable pageable,
                                                 @RequestParam(value = "query", required = false) String query) {
        List<Account> accounts = null;

        if (query != null && !query.trim().isEmpty()) {
            accounts = this.accountService.findByQuery(pageable, query);
        } else {
            accounts = this.accountService.findAll(pageable);
        }

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

}
