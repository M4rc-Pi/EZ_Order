package cat.inspedralbes.projecte2dam.ezorderv2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cat.inspedralbes.projecte2dam.ezorderv2.fragments.CategoriesFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.CistellaFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.HomeFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.ProductesFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.ValoracionsFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.QRScanner;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "--MainActivity";
    private final String URL_PRODUCTES = Constants.URL_TOTS_ELS_PRODUCTES+"?order=id_categoria&order=nom";
    private final String URL_CATEGORIES = Constants.URL_CATEGORIES;

    private LinearLayout menuInferior;
    private List<String> categories;
    private List<Producte> productes;
    private Comanda comanda;
    private int idUsuari;
    private int numTaula;
    private Map<Producte, Integer> cistella;

    private OkHttpClient client;
    private Parser parser;
    private String json;

    //Fragments
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private CategoriesFragment categoriesFragment;
    private ProductesFragment productesFragment;
    private CistellaFragment cistellaFragment;
    private ValoracionsFragment valoracionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuInferior = findViewById(R.id.layoutbuttons_mainactivity_menuinferior);
        menuInferior.setVisibility(View.GONE);

        idUsuari = Integer.parseInt(getIntent().getStringExtra("ID_USUARI"));
        categories = new ArrayList<String>();
        productes = new ArrayList<Producte>();
        cistella = new HashMap<Producte, Integer>();
        comanda = new Comanda(cistella, idUsuari, numTaula);

        //Instancies dels Fragments que intervenen en aquesta activitat
        homeFragment = new HomeFragment();
        homeFragment.setOnClickListener(this::onClickCanviaFragment);
        categoriesFragment = new CategoriesFragment();
        productesFragment = new ProductesFragment();
        cistellaFragment = new CistellaFragment(comanda);
        valoracionsFragment = new ValoracionsFragment();

        //cargar el fragment desde el inicio de la activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main_activity, homeFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Handle QR Scanner Results
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult.getContents() != null){
            comanda.setCodi_taula(Integer.parseInt(intentResult.getContents()));
        }else {
            Random rand = new Random();
            int codiAleatori = rand.nextInt(15)+1;
            comanda.setCodi_taula(codiAleatori);
            Toast.makeText(getApplicationContext(), "No s'ha escanejat res, s'ha assignat la taula "+ codiAleatori, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "codi_taula: " + comanda.getCodi_taula());
        }
        menuInferior.setVisibility(View.VISIBLE);
    }

    /**
     * Metode onClick per als botons de la MainActivity.  Cada vegada que es clica el "button.getId()",
     * es descarreguen els jsons de cada fragment
     * @param view
     */
    public void onClickCanviaFragment(View view){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId()){
            case R.id.imgbutton_home_clients_qrscanner:
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("QR Scanner");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(QRScanner.class);
                intentIntegrator.initiateScan();
                break;

            case R.id.button_main_activity_obrircategories:
                //Connexió amb el server per descarregar dades
                client = new OkHttpClient();

                Request request_categories = new Request.Builder()
                        .url(URL_CATEGORIES)
                        .build();

                client.newCall(request_categories).enqueue(new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onFailure: FAILED");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            json = responseBody.string();

                            //es parseja la descarrega
                            parser = new Parser();
                            Log.d("JSON", "" + json);

                            categories = parser.parsejaCategories(json);
                            categoriesFragment.setCategories(categories);
                            categoriesFragment.setComanda(comanda);
                            fragmentTransaction
                                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                    .replace(R.id.fragment_container_main_activity, categoriesFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.button_main_activity_obrirproductes:
                //Connexió amb el server per descarregar dades
                client = new OkHttpClient();

                Request request_productes = new Request.Builder()
                        .url(URL_PRODUCTES)
                        .build();

                client.newCall(request_productes).enqueue(new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onFailure: FAILED");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            json = responseBody.string();

                            //es parseja la descarrega
                            parser = new Parser();
                            Log.d("JSON", "" + json);

                    productes  = parser.parsejaProductes(json);
                    productesFragment.setProductes(productes);
                    productesFragment.setComanda(comanda);

                    fragmentTransaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container_main_activity, productesFragment)
                            .addToBackStack(null)
                            .commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.button_main_activity_valoracions:
                fragmentTransaction
                        .replace(R.id.fragment_container_main_activity, valoracionsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case  R.id.button_main_activity_obrircistella:
//                cistella.put(new Producte("prova", "prova", 0.00f, 0, 0, 0), 2);
                cistellaFragment.setComanda(comanda);
                fragmentTransaction
                        .replace(R.id.fragment_container_main_activity, cistellaFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }


}