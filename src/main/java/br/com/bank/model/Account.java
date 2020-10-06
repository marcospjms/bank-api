package br.com.bank.model;

import br.com.bank.model.auth.BankUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class Account extends AbstractEntity {

    @Column(unique = true)
    private String code;

    @OneToOne
    private BankUser bankUser;

    @JsonIgnore
    private Double balance;

    public void increaseBalance(Double increase) {
        if (increase < 0) {
            throw new RuntimeException("Não é permitido ficar com saldo negativo");
        }
        this.balance += increase;
    }

    public void decreaseBalance(Double increase) {
        if (this.balance - increase <= 0 || increase < 0) {
            throw new RuntimeException("Não é permitido ficar com saldo negativo");
        }
        this.balance -= increase;
    }
}
