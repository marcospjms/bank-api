package br.com.bank.configs.security;

import br.com.bank.model.auth.BankUser;
import br.com.bank.model.auth.BankUserSpring;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(SecurityConstants.HEADER_AUTH);
        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        BankUser bankUser = this.getBankUser(request);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new BankUserSpring(bankUser),
                null,
                AuthorityUtils.createAuthorityList(bankUser.getStrRoles()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private BankUser getBankUser(HttpServletRequest request) throws IOException {
        String fullToken = request.getHeader(SecurityConstants.HEADER_AUTH);

        if (fullToken == null) {
            return null;
        }

        String token = fullToken.replace(SecurityConstants.TOKEN_PREFIX, "");
        String usuarioJson = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().getSubject();

        return new ObjectMapper().readValue(usuarioJson, BankUser.class);
    }
}
