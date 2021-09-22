package tn.ilyeszouaoui.common;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static boolean verifyPassword(String passwordToVerify, String bCryptPasswordHash) {
        return BCrypt.checkpw(passwordToVerify, bCryptPasswordHash);
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
}
