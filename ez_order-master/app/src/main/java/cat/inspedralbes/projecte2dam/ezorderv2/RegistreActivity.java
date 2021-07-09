package cat.inspedralbes.projecte2dam.ezorderv2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.EMAIL_BD;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.HashGenerator;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Validador;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegistreActivity extends AppCompatActivity {

    OkHttpClient client;
    Parser parser;
    HashGenerator hashGenerator;
    Context context;
    Validador validador;

    //Usuari usuari;
    EditText etNom;
    EditText etCognom;
    EditText etCognom2;
    EditText etMail;
    EditText etPass;
    EditText etPass2;
    String nom;
    String cognom;
    String cognom2;
    String email;
    String pass;
    String pass2;

    String jsonRegistre;
    String jsonEmails;
    String urlRegistre;
    String urlEmails;
    List<String> emails;

    EMAIL_BD estatEmail;

    //boolean emailExisteix = true;

    Map<String, String> dadesRegistre;
//    String regexMail;
//    String regexPass;
//    Pattern patternMail;
//    Pattern patternPass;
//    Matcher matcherMail;
//    Matcher matcherPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre2);

        parser = new Parser();
        client = new OkHttpClient();
        dadesRegistre = new HashMap<>();
        hashGenerator = new HashGenerator();
        emails = new ArrayList<>();

        etNom = findViewById(R.id.editText_registre_nom);
        etCognom = findViewById(R.id.editText_registre_cognom);
        etCognom2 = findViewById(R.id.editText_registre_cognom2);
        etMail = findViewById(R.id.editText_registre_mail);
        etPass = findViewById(R.id.editText_registre_pass);
        etPass2 = findViewById(R.id.editText_registre_pass2);

        urlRegistre = Constants.URL_USUARIS;
        urlEmails = Constants.URL_USUARIS+"?include=email";

//        regexMail = "^(.+)@(.+)$";
//        patternMail = Pattern.compile(regexMail);
//        regexPass = "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*!?¿&=)(¡]).{8,20}$";
//        patternPass = Pattern.compile(regexPass);

        estatEmail = EMAIL_BD.EMAIL_EN_PROCES;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickRegistrar(View view) throws NoSuchAlgorithmException {

        context = view.getContext();
        validador = new Validador(context);

        nom = etNom.getText().toString();
        cognom = etCognom.getText().toString();
        cognom2 = etCognom2.getText().toString();
        email = etMail.getText().toString();
        pass = etPass.getText().toString();
        pass2 = etPass2.getText().toString();

        dadesRegistre.put("nom", nom);
        dadesRegistre.put("cognom", cognom);
        dadesRegistre.put("cognom2", cognom2);
        dadesRegistre.put("email", email);
        dadesRegistre.put("pass", pass);
        dadesRegistre.put("pass2", pass2);

//        jsonRegistre = "{\n" +
//                "      \"nom\": \""+nom+"\",\n" +
//                "      \"cognom1\": \""+cognom+"\",\n" +
//                "      \"cognom2\": \""+cognom2+"\",\n" +
//                "      \"email\": \""+email+"\",\n" +
//                "      \"password\": \""+pass+"\",\n" +
//                "      \"tipus\": \"client\"\n" +
//                "    }";
//        Log.d("onClickRegistrar", "Usuari a Registrar: "+jsonRegistre);

        //usuari = new Usuari(nom, cognom, cognom2, email, pass);

        /**
         * Fer consulta a la BD per veure si l'email existeix. Si no existeix, la variable "emailExisteix" es posa a "false".
         * Després es comprova que tots els camps siguin correctes amb el validador.
         * Si tot està bé, es fa l'intent a la vista del Login.
         */


        if (validador.validarRegistre(dadesRegistre)) {
            Log.d("onClickRegistrar", "S'han validat els camps correctament");
            emailsRequest(urlEmails);

            while (estatEmail == EMAIL_BD.EMAIL_EN_PROCES) {
                Log.d("onClickRegistrar", "Comprovant si l'email "+email+" ja es troba a la BD");

                if (estatEmail == EMAIL_BD.EMAIL_EXISTEIX) {
                    Log.d("onClickRegistrar", "EMAIL EXISTEIX");
                    Toast.makeText(context, "L'email "+email+" ja està en ús.", Toast.LENGTH_LONG).show();
                    estatEmail = EMAIL_BD.EMAIL_EN_PROCES;
                    break;
                } else if (estatEmail == EMAIL_BD.EMAIL_NO_EXISTEIX) {
                    Log.d("onClickRegistrar", "EMAIL NO EXISTEIX");

                    byte[] passHash = HashGenerator.getSHA(pass);
                    //byte[] pass2Hash = HashGenerator.getSHA(pass2);
                    pass = HashGenerator.toHexString(passHash);
                    //pass2 = HashGenerator.toHexString(pass2Hash);

                    jsonRegistre = "{\n" +
                            "      \"nom\": \""+nom+"\",\n" +
                            "      \"cognom1\": \""+cognom+"\",\n" +
                            "      \"cognom2\": \""+cognom2+"\",\n" +
                            "      \"email\": \""+email+"\",\n" +
                            "      \"password\": \""+pass+"\",\n" +
                            "      \"tipus\": \"client\"\n" +
                            "    }";
                    Log.d("onClickRegistrar", "Usuari a Registrar: "+jsonRegistre);

                    registreRequest(urlRegistre);
                }
            }
        }
    }

    public void registreRequest (String url){

        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonRegistre);

        Request request_insert = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request_insert).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("OkHttp: ", "onFailure: FAILED");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Log.d("OkHttp2: ", "onResponse: " + body.toString());

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);

                    runOnUiThread(() -> Toast.makeText(context, nom+" t'has registrat correctament!", Toast.LENGTH_LONG).show());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void emailsRequest(String url) {

        Request request_categories = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request_categories).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("LoginRequest", "onFailure: FAILED");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);


                    jsonEmails = responseBody.string();
                    Log.d("JSON", "" + jsonEmails);

                    try {
                        //Es parseja la descarrega
                        emails = parser.parsejarEmails(jsonEmails);

                        if (!emails.contains(email)){
                            estatEmail = EMAIL_BD.EMAIL_NO_EXISTEIX;
                        } else {
                            estatEmail = EMAIL_BD.EMAIL_EXISTEIX;
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}