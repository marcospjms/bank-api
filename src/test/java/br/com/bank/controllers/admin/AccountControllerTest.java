package br.com.bank.controllers.admin;

import br.com.bank.model.Account;
import br.com.bank.model.auth.BankUser;
import br.com.bank.util.tests.AdminRestTemplate;
import br.com.bank.util.tests.UtilTestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class AccountControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AdminRestTemplate adminRestTemplate;

    @Autowired
    private UtilTestService utilTestService;

    private String serveUrl;
    private String accountsUrl;

    @BeforeEach
    public void setup() {
        this.serveUrl = "http://localhost:" + port + "/api/";
        this.accountsUrl = this.serveUrl + "admin/accounts";
    }

    @Test
    public void createValidAccountTest() throws Exception {
        BankUser user = this.utilTestService.customer;
        assertThat(this.adminRestTemplate.postForObject(this.accountsUrl, user, Account.class))
                .hasFieldOrProperty("code")
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("bankUser.name", user.getName())
                .hasFieldOrPropertyWithValue("bankUser.password", null);
    }

    @Test
    public void createInvalidCategoryTest() throws Exception {
        BankUser bankUser = new BankUser();
        Assertions.assertThrows(Exception.class, () ->
                this.adminRestTemplate.postForObject(this.accountsUrl, bankUser, Account.class));
    }
}
