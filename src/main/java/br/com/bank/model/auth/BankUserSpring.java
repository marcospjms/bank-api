package br.com.bank.model.auth;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class BankUserSpring extends User {

    private BankUser bankUser;

    public BankUserSpring(BankUser bankUser) {
        super(bankUser.getUsername(), bankUser.getPassword(), AuthorityUtils.createAuthorityList(bankUser.getStrRoles()));
        this.bankUser = bankUser;
    }

    public BankUser getBankUser() {
        return bankUser;
    }
}
