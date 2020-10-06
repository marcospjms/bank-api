package br.com.bank.configs.security;

import br.com.bank.model.auth.BankUser;
import br.com.bank.model.auth.BankUserSpring;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManger;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManger
    ) {
        this.authenticationManger = authenticationManger;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            BankUser bankUser = new ObjectMapper().readValue(request.getInputStream(), BankUser.class);

            return this.authenticationManger.authenticate(
                    new UsernamePasswordAuthenticationToken(bankUser.getUsername(), bankUser.getPassword())
            );
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authResult;

        BankUserSpring bankUserSpring = (BankUserSpring) authenticationToken.getPrincipal();
        BankUser bankUser = bankUserSpring.getBankUser();
        bankUser.setPassword("");
        String bankUserSpringJson = new ObjectMapper().writeValueAsString(bankUser);
        String token = Jwts.builder()
                .setSubject(bankUserSpringJson)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

        response.setContentType("application/json");

        response.getWriter().write( " { \"token\" : \""+ token + "\" }");
    }
}
