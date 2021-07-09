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
import java.util.HashMap;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.HashGenerator;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    OkHttpClient client;
    Parser parser;
    HashGenerator hashGenerator;
    Context context;

    String json;
    EditText etMail;
    EditText etPass;
    String email;
    String pass;
    String urlLogin;
    int num_taula;
    Map<String,String> resultatLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parser = new Parser();
        resultatLogin = new HashMap<>();
        hashGenerator = new HashGenerator();
        client = new OkHttpClient();
        etMail = findViewById(R.id.editText_login_mail);
        etPass = findViewById(R.id.editText_login_pass);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickEnvia(View view) throws NoSuchAlgorithmException {
        context = view.getContext();
//        Log.d("onClickEnvia_Edit", "etMail: "+etMail+ " etPass: "+ etPass);
        email = etMail.getText().toString();
        pass = etPass.getText().toString();
        byte[] passHash = HashGenerator.getSHA(pass);
        pass = HashGenerator.toHexString(passHash);
        urlLogin = Constants.URL_USUARIS+"?filter=email,eq,"+email+"&filter=password,eq,"+pass;
        Log.d("onClickEnvia", "URL: "+urlLogin);
        loginRequest(urlLogin);
    }

    public void onClickRegistre(View view) {
        Intent intent = new Intent(view.getContext(), RegistreActivity.class);
        startActivity(intent);
    }

    public void loginRequest(String url){
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
                    json = responseBody.string();
                    Log.d("JSON", "" + json);
                    try {
                        //Es parseja la descarrega
                        resultatLogin = parser.parsejarLogin(json);
//                        Log.d("OnResponseLogin", "PassLocal: "+pass);
//                        Log.d("OnResponseLogin", "PassBD: "+resultatLogin.get("password"));
                        if (email.equals(resultatLogin.get("email")) && pass.equals(resultatLogin.get("password"))) {
                            if (resultatLogin.get("tipus").equals("client")) {
                                //FER INTENT A LA VISTA "HOME"
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("ID_USUARI", resultatLogin.get("id"));
                                intent.putExtra("NUM_TAULA", num_taula);
                                startActivity(intent);
                            } else {
                                //FER INTENT A LA VISTA DE L'EMPRESA
                                Intent intent = new Intent(context, EmpresaActivity.class);
                                intent.putExtra("ID_USUARI", resultatLogin.get("id"));
                                intent.putExtra("NUM_TAULA", num_taula);
                                startActivity(intent);
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(context, "Les credencials proporcionades no són correctes.", Toast.LENGTH_LONG).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}