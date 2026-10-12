package pe.edu.utp.siged.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Verificacion de claves con PBKDF2 (RNF03: nunca texto plano). Formato guardado: saltHex$hashHex. */
public final class PasswordUtil {

    private static final int ITERACIONES = 65000;
    private static final int BITS = 256;

    private PasswordUtil() { }

    public static boolean verificar(String claveIngresada, String almacenado) {
        if (claveIngresada == null || almacenado == null || !almacenado.contains("$")) {
            return false;
        }
        String[] partes = almacenado.split("\\$");
        try {
            byte[] salt = hex(partes[0]);
            PBEKeySpec spec = new PBEKeySpec(claveIngresada.toCharArray(), salt, ITERACIONES, BITS);
            byte[] calculado = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(calculado, hex(partes[1]));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] hex(String s) {
        byte[] r = new byte[s.length() / 2];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return r;
    }
}
