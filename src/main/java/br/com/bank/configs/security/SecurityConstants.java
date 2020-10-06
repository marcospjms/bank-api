package br.com.bank.configs.security;

public class SecurityConstants {

    public static final String SECRET = "TESTANDOAQUI";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTH = "Authorization";
    public static final Long EXPIRATION_TIME = 86400000L; // Um dia em milisegundos
}
