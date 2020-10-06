package br.com.bank.controllers.admin;

import br.com.bank.model.Account;
import br.com.bank.model.Transaction;
import br.com.bank.model.TransactionType;
import br.com.bank.services.AccountService;
import br.com.bank.util.tests.CustomerRestTemplate;
import br.com.bank.util.tests.UtilTestService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
public class TransactionControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRestTemplate customerRestTemplate;

    @Autowired
    private UtilTestService utilTestService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EntityManager em;

    private String serveUrl;
    private String accountsUrl;

    @BeforeEach
    public void setup() {
        this.serveUrl = "http://localhost:" + port + "/api/";
        this.accountsUrl = this.serveUrl + "user/transactions";
    }

    @Test
    public void validTransferTest() throws Exception {
        Account sourceAccount = account(this.utilTestService.account);
        Account targetAccount = account(this.utilTestService.account2);
        Double sourceBalance = sourceAccount.getBalance();
        Double targetBalance = targetAccount.getBalance();
        Double valueToTransfer = 500d;
        Transaction transaction = new Transaction();
        transaction.setSource(sourceAccount);
        transaction.setTarget(targetAccount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setValue(valueToTransfer);

        Transaction result = this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class);
        assertThat(result)
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("value")
                .hasFieldOrProperty("source")
                .hasFieldOrProperty("target");
        em.refresh(sourceAccount);
        em.refresh(targetAccount);
        assertThat(sourceAccount.getBalance()).isEqualTo(sourceBalance - valueToTransfer);
        assertThat(targetAccount.getBalance()).isEqualTo(targetBalance + valueToTransfer);
    }

    @Test
    @Order(2)
    public void insufficientValueTransferTest() throws Exception {
        Account sourceAccount = account(this.utilTestService.account);
        Account targetAccount = account(this.utilTestService.account2);
        Double valueToTransfer = Double.MAX_VALUE;
        Transaction transaction = new Transaction();
        transaction.setSource(sourceAccount);
        transaction.setTarget(targetAccount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setValue(valueToTransfer);

        Assertions.assertThrows(Exception.class, () ->
                this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class)
        );
    }

    @Test
    @Order(3)
    public void negativeValueTransferTest() throws Exception {
        Account sourceAccount = account(this.utilTestService.account);
        Account targetAccount = account(this.utilTestService.account2);
        Double valueToTransfer = -Double.MAX_VALUE;
        Transaction transaction = new Transaction();
        transaction.setSource(sourceAccount);
        transaction.setTarget(targetAccount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setValue(valueToTransfer);

        Assertions.assertThrows(Exception.class, () ->
                this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class)
        );
    }

    @Test
    @Order(4)
    public void validDepositTest() throws Exception {
        Account targetAccount = account(this.utilTestService.account);
        Double targetBalance = targetAccount.getBalance();
        Double valueToDeposit = 500d;
        Transaction transaction = new Transaction();
        transaction.setTarget(targetAccount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setValue(valueToDeposit);

        assertThat(this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class))
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("value")
                .hasFieldOrPropertyWithValue("source", null)
                .hasFieldOrProperty("target");
        em.refresh(targetAccount);
        assertThat(this.accountService.findById(targetAccount.getId()).getBalance()).isEqualTo(targetBalance + valueToDeposit);
    }

    @Test
    @Order(5)
    public void invalidDepositTest() throws Exception {
        Account targetAccount = account(this.utilTestService.account);
        Double valueToDeposit = -500d;
        Transaction transaction = new Transaction();
        transaction.setTarget(targetAccount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setValue(valueToDeposit);

        Assertions.assertThrows(Exception.class, () ->
                this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class)
        );
    }

    @Test
    @Order(6)
    public void validWithdrawalsTest() throws Exception {
        Account sourceAccount = account(this.utilTestService.account);
        Double sourceBalance = sourceAccount.getBalance();
        Double valueToWithdrawals = 50d;
        Transaction transaction = new Transaction();
        transaction.setSource(sourceAccount);
        transaction.setType(TransactionType.WITHDRAWALS);
        transaction.setValue(valueToWithdrawals);

        assertThat(this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class))
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("value")
                .hasFieldOrProperty("source")
                .hasFieldOrPropertyWithValue("target", null);
        em.refresh(sourceAccount);
        assertThat(this.accountService.findById(sourceAccount.getId()).getBalance()).isEqualTo(sourceBalance - valueToWithdrawals);
    }

    @Test
    @Order(7)
    public void invalidWithdrawalsTest() throws Exception {
        Account sourceAccount = account(this.utilTestService.account);
        Double valueToWithdrawals = -5000d;
        Transaction transaction = new Transaction();
        transaction.setSource(sourceAccount);
        transaction.setType(TransactionType.WITHDRAWALS);
        transaction.setValue(valueToWithdrawals);

        Assertions.assertThrows(Exception.class, () ->
                this.customerRestTemplate.postForObject(this.accountsUrl, transaction, Transaction.class)
        );
    }

    private Account account(Account account) {
        return this.accountService.findById(account.getId());
    }

}
