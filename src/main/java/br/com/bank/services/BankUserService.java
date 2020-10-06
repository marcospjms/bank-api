package br.com.bank.services;

import br.com.bank.model.auth.Role;
import br.com.bank.model.auth.BankUser;
import br.com.bank.repositories.BankUserRepository;
import br.com.bank.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class BankUserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final BankUserRepository repository;

    private final AccountService accountService;

    public BankUserService(BankUserRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    public BankUser createUser(BankUser bankUser, boolean admin) {
        bankUser.setId(null);
        bankUser.setRoles(new HashSet<Role>(){{
            add(Role.CUSTOMER);
            if (admin) {
                add(Role.ADMIN);
            }
        }});
        bankUser.setPassword(this.encoder.encode(bankUser.getPassword()));
        repository.save(bankUser);
        accountService.createAccount(bankUser);
        bankUser.setPassword(null);
        return bankUser;
    }

    /**
     * Evita alterações não permitidas nos dados dos usuários
     */
    public BankUser updateFromBankUser(String username, BankUser bankUser) {
        BankUser savedUser = this.findByUsername(username);

        bankUser.setId(savedUser.getId());
        bankUser.setUsername(savedUser.getUsername());
        bankUser.setPassword(savedUser.getPassword());
        bankUser.setRoles(savedUser.getRoles());

        return this.save(bankUser);
    }

    public BankUser updatePassword(String username, String newPassword) {
        BankUser bankUser = this.findByUsername(username);
        bankUser.setPassword(this.encoder.encode(newPassword));
        return repository.save(bankUser);
    }

    public BankUser save(BankUser bankUser) {
        return this.repository.save(bankUser);
    }

    public boolean delete(Long userId) {
        this.repository.deleteById(userId);
        return true;
    }

    public List<BankUser> findAll(Pageable pageable) {
        List<BankUser> users = repository.findAll(pageable);
        users.stream().forEach(u -> u.setPassword(null));
        return users;
    }

    public BankUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<BankUser> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        List<BankUser> usuarios = repository.findAll(AbstractEntityUtil.createExample(new BankUser(), query), pageable).getContent();
        usuarios.stream().forEach(u -> u.setPassword(null));
        return usuarios;
    }

    public BankUser findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }
}
