package br.com.bank.controllers;

import br.com.bank.model.auth.BankUser;
import br.com.bank.services.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/public/users")
public class CustomerPublicController {

    @Autowired
    private BankUserService userService;

    @PostMapping(value = "")
    public ResponseEntity<BankUser> createBankUser(@RequestBody BankUser bankUser) {
        return new ResponseEntity<>(this.userService.createUser(bankUser, false), HttpStatus.OK);
    }

    /**
     * Para facilitar o teste sem precisar mexer no banco.
     */
    @PostMapping(value = "admin")
    public ResponseEntity<BankUser> createBankUserAdmin(@RequestBody BankUser bankUser) {
        return new ResponseEntity<>(this.userService.createUser(bankUser, true), HttpStatus.OK);
    }

    public ResponseEntity<String> listAll(Pageable pageable) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
