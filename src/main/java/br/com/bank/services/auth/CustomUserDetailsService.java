package br.com.bank.services.auth;

import br.com.bank.model.auth.BankUser;
import br.com.bank.model.auth.BankUserSpring;
import br.com.bank.services.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BankUserService bankUserService;

    @Override
    public BankUserSpring loadUserByUsername(String username) throws UsernameNotFoundException {
        BankUser user = Optional.ofNullable(this.bankUserService.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new BankUserSpring(user);
    }
}
