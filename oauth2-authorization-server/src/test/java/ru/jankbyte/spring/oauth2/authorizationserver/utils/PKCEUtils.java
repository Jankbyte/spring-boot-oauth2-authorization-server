package ru.jankbyte.spring.oauth2.authorizationserver.utils;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getEncoder;

public final class PKCEUtils {
    private PKCEUtils() {}

    public static String getCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(codeVerifier.getBytes(UTF_8));
            byte[] encoded = getEncoder().encode(hashBytes);
            return new String(encoded).replaceAll("\\+", "-")
                .replaceAll("\\/", "_")
                .replaceAll("=", "");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
