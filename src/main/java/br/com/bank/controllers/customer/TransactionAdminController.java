package br.com.bank.controllers.customer;

import br.com.bank.model.Transaction;
import br.com.bank.model.auth.BankUser;
import br.com.bank.services.BankUserService;
import br.com.bank.services.TransactionService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/transactions")
public class TransactionAdminController {

    private final TransactionService transactionService;

    private final BankUserService userService;

    public TransactionAdminController(TransactionService transactionService, BankUserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Transaction> execute(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Transaction transaction) {
        BankUser customer = this.userService.findByUsername(userDetails.getUsername());
        return new ResponseEntity<>(this.transactionService.execute(customer, transaction), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> get(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable(value = "id") Long id) {
        BankUser customer = this.userService.findByUsername(userDetails.getUsername());
        return new ResponseEntity<>(this.transactionService.findByIdAndBankUser(id, customer), HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Transaction>> listAll(Pageable pageable,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        BankUser customer = this.userService.findByUsername(userDetails.getUsername());
        return new ResponseEntity<>(this.transactionService.findByDateAndUser(pageable, DateTime.now(), customer), HttpStatus.OK);
    }

}
