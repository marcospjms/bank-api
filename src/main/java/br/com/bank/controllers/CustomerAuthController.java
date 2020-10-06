package br.com.bank.controllers;

import br.com.bank.model.auth.BankUser;
import br.com.bank.services.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/users")
public class CustomerAuthController {

    @Autowired
    private BankUserService userService;

    @PutMapping(value = "")
    public ResponseEntity<BankUser> update(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody BankUser bankUser) {
        return new ResponseEntity<>(this.userService.updateFromBankUser(userDetails.getUsername(), bankUser), HttpStatus.OK);
    }

    @PutMapping(value = "password")
    public ResponseEntity<BankUser> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody BankUser bankUser) {
        return new ResponseEntity<>(this.userService.updatePassword(userDetails.getUsername(), bankUser.getPassword()), HttpStatus.OK);
    }

    @GetMapping(value = "current")
    public ResponseEntity<BankUser> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        BankUser customer = this.userService.findByUsername(userDetails.getUsername());
        customer.setPassword(null);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
