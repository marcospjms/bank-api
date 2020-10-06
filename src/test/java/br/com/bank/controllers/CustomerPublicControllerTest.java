package br.com.bank.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.bank.model.auth.Role;
import br.com.bank.model.auth.BankUser;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.transaction.Transactional;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerPublicControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String serveUrl;
    private String usersUrl;
    private String createPublicUserUrl;
    private String createAdminUserUrl;

    @BeforeEach
    public void setup() {
        this.serveUrl = "http://localhost:" + port + "/api/";
        this.usersUrl = this.serveUrl + "public/users/";
        this.createPublicUserUrl = this.usersUrl;
        this.createAdminUserUrl = this.usersUrl + "admin";
    }

    @Test
    public void createValidPublicUserTest() throws Exception {
        BankUser bankUser = new BankUser();
        bankUser.setUsername("usuariopublico");
        bankUser.setPassword("123456");
        bankUser.setEmail("usuariopublico@gmail.com");
        bankUser.setName("Teste da Silva");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, bankUser, BankUser.class))
                .hasFieldOrPropertyWithValue("username", bankUser.getUsername())
                .hasFieldOrPropertyWithValue("email", bankUser.getEmail())
                .hasFieldOrPropertyWithValue("name", bankUser.getName())
                .hasFieldOrPropertyWithValue("password", null)
                .extracting("roles")
                .asInstanceOf(InstanceOfAssertFactories.ITERABLE)
                .containsOnly(Role.CUSTOMER);
    }

    @Test
    public void createInvalidPublicUserTest() throws Exception {
        BankUser bankUser = new BankUser();
        bankUser.setUsername("usuariopublicoinvalido");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, bankUser, BankUser.class)).hasAllNullFieldsOrProperties();
    }

    @Test
    public void createValidAdminUserTest() throws Exception {
        BankUser bankUser = new BankUser();
        bankUser.setUsername("usuarioadmin");
        bankUser.setPassword("123456");
        bankUser.setEmail("usuarioadmin@gmail.com");
        bankUser.setName("Teste da Silva");
        assertThat(this.restTemplate.postForObject(this.createAdminUserUrl, bankUser, BankUser.class))
                .hasFieldOrPropertyWithValue("username", bankUser.getUsername())
                .hasFieldOrPropertyWithValue("email", bankUser.getEmail())
                .hasFieldOrPropertyWithValue("name", bankUser.getName())
                .hasFieldOrPropertyWithValue("password", null)
                .extracting("roles")
                .asInstanceOf(InstanceOfAssertFactories.ITERABLE)
                .containsOnly(Role.CUSTOMER, Role.ADMIN);
    }

    @Test
    public void createInvalidAdminUserTest() throws Exception {
        BankUser bankUser = new BankUser();
        bankUser.setUsername("usuarioadmininvalido");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, bankUser, BankUser.class)).hasAllNullFieldsOrProperties();
    }
}
