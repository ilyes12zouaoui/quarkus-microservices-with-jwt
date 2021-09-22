package tn.ilyeszouaoui.common;

import io.smallrye.jwt.build.Jwt;

import java.util.Collections;
import java.util.HashSet;

public class JWTUtils {

    public static String generateJWT(int id, String firstName, String lastName, String email, String role, int age) {
        return Jwt.issuer("https://example.com/issuer")
                .upn("ilyes@gmail.com")
                .groups(new HashSet<>(Collections.singletonList(role)))
                .claim("id", id)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .claim("email", email)
                .claim("role", role)
                .claim("age", age)
                .sign();
    }

}
