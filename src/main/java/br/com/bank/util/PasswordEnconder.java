package br.com.bank.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEnconder {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        $2a$10$7KHSySqYhdfmAu7fY4aX.eauMV5TPjO9qAu3QQxnRcuXoOivqVbMq
        System.out.println(passwordEncoder.encode("1234567"));
    }
}
