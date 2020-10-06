package br.com.bank.util.tests;

import br.com.bank.configs.security.SecurityConstants;
import br.com.bank.model.auth.BankUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

@Service
public class CustomerRestTemplate extends RestTemplate {

    @Autowired
    private UtilTestService utilTestService;

    @PostConstruct
    public void setup() {
        this.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                int port = request.getURI().getPort();
                BankUser customer = CustomerRestTemplate.this.utilTestService.customer;
                System.out.println(customer);
                String storeUserSpringJson = new ObjectMapper().writeValueAsString(customer);
                String token = Jwts.builder()
                        .setSubject(storeUserSpringJson)
                        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                        .compact();

                request.getHeaders().add(SecurityConstants.HEADER_AUTH, SecurityConstants.TOKEN_PREFIX + token);

                return execution.execute(request, body);
            }
        });

    }
}
