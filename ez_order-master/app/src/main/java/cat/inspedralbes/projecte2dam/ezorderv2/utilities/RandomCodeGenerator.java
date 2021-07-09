package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

import java.util.Random;

/**
 * Aquesta classe genera codis alfanumerics [A-Z / 0-9] aleatoris.
 * @author Marc Picas Hervas
 * @version May/2021
 */
public class RandomCodeGenerator {

    String code;

    /**
     * El mètode retorna un codi alfanumeric aleatori.<br>
     * @return
     */
    public String generarCodiRandom() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 10) {
            int index = (int) (random.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String generatedString = salt.toString();
        code = generatedString;
        return code;
    }

    /**
     * Aquest mètode retorna un numero aleatori dins del rang mínim-màxim indicat als paràmetres.<br>
     * @param min
     * @param max
     * @return
     */
    public  static int generarNumberoRandomRange(int min, int max) {
        Random random = new Random();
        int res = random.nextInt(max - min+1) + min;
        return res;
    }

}
