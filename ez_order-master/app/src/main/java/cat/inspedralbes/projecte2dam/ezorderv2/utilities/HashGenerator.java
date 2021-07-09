package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] getSHA(String pass) throws NoSuchAlgorithmException {
        byte[] dades = pass.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(dades);
    }

    //Serveix per passar el hash en forma d'array de bytes a String

    public static String toHexString(byte[] hash) {

        BigInteger numero = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(numero.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
