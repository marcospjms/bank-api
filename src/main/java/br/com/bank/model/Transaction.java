package br.com.bank.model;

import br.com.bank.model.auth.BankUser;
import br.com.bank.util.DateTimeDeserializer;
import br.com.bank.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Transaction extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private Account source;

    @ManyToOne
    private Account target;

    private Double value;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    protected DateTime executedDate;

    public boolean canTransfer(BankUser loggedUser) {
        return this.getSource() != null && this.getTarget() != null && loggedUser.equals(this.getSource().getBankUser());
    }

    public boolean canDeposit(BankUser loggedUser) {
        return this.getTarget() != null && loggedUser.equals(this.getTarget().getBankUser());
    }

    public boolean canWithdrawals(BankUser loggedUser) {
        return this.getSource() != null && loggedUser.equals(this.getSource().getBankUser());
    }

}
