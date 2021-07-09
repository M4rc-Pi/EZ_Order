package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cat.inspedralbes.projecte2dam.ezorderv2.EmpresaActivity;
import cat.inspedralbes.projecte2dam.ezorderv2.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Validador {

    OkHttpClient client;
    Parser parser;

    String regexMail;
    String regexPass;
    Pattern patternMail;
    Pattern patternPass;
    Matcher matcherMail;
    Matcher matcherPass;
    Context context;

    String email;
    String pass;
    String pass2;

    String json;
    List<String> emails;

    public Validador(Context context) {
        this.context = context;
        client = new OkHttpClient();
        parser = new Parser();
    }


    public boolean validarRegistre(Map<String, String> dadesRegistre) {

        regexMail = "^(.+)@(.+)$";
        patternMail = Pattern.compile(regexMail);
        //regexPass = "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*!?¿&=)(¡]).{8,20}$";
        regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        patternPass = Pattern.compile(regexPass);

        email = dadesRegistre.get("email");
        pass = dadesRegistre.get("pass");
        pass2 = dadesRegistre.get("pass2");

        //&& comprovarFormatContrasenya(pass)

        if (comprovarCampsNullBuit(dadesRegistre) && comprovarFormatEmail(email) && comprovarFormatContrasenya(pass) && comprovarRepeticioContrasenya(pass, pass2)) {
            return true;
        }
        return false;
    }

    public boolean comprovarCampsNullBuit(Map<String, String> mapa) {

        for (String dada : mapa.values()) {

            if (dada == null || dada.isEmpty() || dada.trim().isEmpty()) {
                Toast.makeText(context, "T'has deixat camps buids. Tots els camps són obligatoris.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean comprovarFormatEmail(String email) {

        matcherMail = patternMail.matcher(email);

        if (matcherMail.matches()) {
            return true;
        }
        Toast.makeText(context, "El camp Email no té format correcte.", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean comprovarFormatContrasenya(String pass) {
        matcherPass = patternPass.matcher(pass);

        if (matcherPass.matches()) {
            return true;
        }
        Toast.makeText(context, "El camp Contrasenya ha de contenir: " +
                "\n - 1 caràcter Númeric, " +
                "\n - 1 caràcter en Minúscules, " +
                "\n - 1 caràcter en Majúscules, " +
                "\n - 1 caràcter Especial d'aquests '@#$%^&+=' " +
                "\n - Longitud superior a 8 caràcters.", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean comprovarRepeticioContrasenya(String pass, String pass2) {

        if (pass.equals(pass2)) {
            return true;
        }
        Toast.makeText(context, "Les contrasenyes no coincideixen.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
