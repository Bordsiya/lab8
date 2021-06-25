package utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Hasher for password
 * @author NastyaBordun
 * @version 1.1
 */

public class PasswordHasher {

    public static String hashPassword(String password) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String hex = String.format("%064x", new BigInteger(1, digest));
            return hex;
        }
        catch (NoSuchAlgorithmException e){
            return password;
        }

    }

}
