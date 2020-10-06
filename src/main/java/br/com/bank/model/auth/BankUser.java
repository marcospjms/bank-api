package br.com.bank.model.auth;

import br.com.bank.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class BankUser extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @JsonIgnore
    public String[] getStrRoles() {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        return this.roles.stream().map(role -> "ROLE_" + role.toString()).collect(Collectors.toList()).toArray(new String[] {});
    }

}
