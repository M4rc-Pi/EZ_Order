package cat.inspedralbes.projecte2dam.ezorderv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa.CuinaFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa.FormulariAfegirProductesFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa.HomeEmpresaFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.ProductesFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa.TaulesFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 */
public class EmpresaActivity extends AppCompatActivity {

    private final String TAG = "*-*-*! EmpresaActivity";
    private final String URL_PRODUCTES = Constants.URL_TOTS_ELS_PRODUCTES + "?order=id_categoria&order=nom";
    private final String URL_CISTELLA = Constants.URL_COMANDES;

    TextView tvLeyendaTaules;
    TextView tvLeyendaComandes;
    TextView tvLeyendaCuina;

    List<Producte> productes;
    Comanda comanda;
    ArrayList<Comanda> comandes;
    int idUsuari;
    int numTaula;
    Map<Producte, Integer> cistella;

    private OkHttpClient client;
    Parser parser;
    String json;

    //Fragments
    FragmentTransaction fragmentTransaction;
    HomeEmpresaFragment homeEmpresaFragment;
    ProductesFragment productesFragment;
    CuinaFragment cuinaFragment;
    TaulesFragment taulesFragment;
    FormulariAfegirProductesFragment formulariAfegirProductesFragment;
//    ResumComandaFragment resumComandaFragment;   //faltaria fer una vista que mostri resum quan el cambrer ha pres la comanda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        handleIntent(getIntent());

        idUsuari = Integer.parseInt(getIntent().getStringExtra("ID_USUARI"));
        numTaula = getIntent().getIntExtra("NUM_TAULA",1);

        // Iniciar les estructures de dades
        productes = new ArrayList<Producte>(); //carreguen la vista ProductesFragment
        cistella = new HashMap<Producte, Integer>();
        comanda = new Comanda(cistella, idUsuari, numTaula);
        comandes = new ArrayList<Comanda>();

        //instancies dels fragments
        homeEmpresaFragment = new HomeEmpresaFragment();
        productesFragment = new ProductesFragment();
        cuinaFragment = new CuinaFragment();
        taulesFragment = new TaulesFragment();
        formulariAfegirProductesFragment = new FormulariAfegirProductesFragment();
//        resumComandaFragment = new ResumComandaFragment();

//        fabCistella = findViewById(R.id.fab_empresa_activity_cistella);
//        fabCistella.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                comanda.setEstatComanda("ACCEPTADA");
//                Log.d("OnClick_Cistella", "Boto Enviar Apretat");
//
//                client = new OkHttpClient();
////                Comanda comanda = new Comanda(cistella);
//
//                final MediaType JSON
//                        = MediaType.parse("application/json; charset=utf-8");
//
//                List<String> json_llistat = comanda.getJsonComanda();
//                Log.d("onClick_Cistella_LIST", "LLISTA JSON"+ json_llistat);
//
//                int contador = 0;
//
//                for (String json_comanda : json_llistat ) {
//
//                    request(ConstantsProjecte.URL_COMANDES, client, json_comanda);
//                    Log.d("for OnClick", "CONTADOR:" + contador++);
//
//                }
//                comanda.getMapa().clear();
//                Log.i("MAPA COMANDA", comanda.getMapa().toString());
//                //TODO enviar comanda a la BD
//                //TODO carregar un nou fragment que ensenyi el resum de la comanda i tingui un EditText per assignar el num. Taula de la Comanda
//
//                Toast.makeText(getApplicationContext(), "COMANDA ENVIADA", Toast.LENGTH_SHORT).show();
//                fabComanda.startAnimation(fabClose);
//                fabCistella.startAnimation(fabClose);
//                isOpen = false;
//
////                getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.fragment_container_main_activity, resumComandaFragment)
////                        .addToBackStack(null)
////                        .commit();
//            }
//        });


        /**
         * Crear nova comanda.
         */
//        fabComanda = findViewById(R.id.fab_empresa_activity_novacomanda);
//        fabComanda.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cistella = new HashMap<Producte, Integer>();
//                comanda = new Comanda(cistella, idUsuari, numTaula);
//                Toast.makeText(getApplicationContext(), "NOVA COMANDA", Toast.LENGTH_SHORT).show();
//                fabComanda.startAnimation(fabClose);
//                fabCistella.startAnimation(fabClose);
//                isOpen = false;
//            }
//        });
//        fabComanda.startAnimation(fabClose);
//        fabCistella.startAnimation(fabClose);

        tvLeyendaComandes = findViewById(R.id.textview_empresa_activity_comandes);
        tvLeyendaCuina = findViewById(R.id.textview_empresa_activity_cuina);
        tvLeyendaTaules = findViewById(R.id.textview_empresa_activity_taules);

        //amagar l'ActionBar
        getSupportActionBar().hide();

        client = new OkHttpClient();

        // la descarrega dels productes pot fer-se onCreateView perque nomes es fa una vegada
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
                    parser = new Parser();
                    Log.d("JSON", "" + json);
                    productes = parser.parsejaProductes(json);
                    productesFragment.setProductes(productes);
                    productesFragment.setComanda(comanda);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_empresa_activity, homeEmpresaFragment)
                .addToBackStack(null)
                .commit();
    }

    //els dos metodes a continuació són per gestionar les cerques a la searchbar (pensO)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "QUERY - handleIntent: " + query);
        }
    }

    /**
     * Menu d'Empresa
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa_activity, menu);

        //SEARCHBAR
        //Associar la configuració searchable amb la vista del menú
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
//        searchView.setSubmitButtonEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.novacomanda_menu_empresa_activity:
                //mostra dialog per introduir numero de taula
                EditText editText = new EditText(getApplicationContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(EmpresaActivity.this);
                builder.setTitle("Taula:");
                builder.setMessage("Introdueix el numero de taula:");
                builder.setView(editText);
                builder.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taula = editText.getText().toString();
                        numTaula = Integer.parseInt(taula);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "No has sel·leccionat cap taula", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();

                idUsuari = Constants.ID_USUARI_CAMBRER; //AQUEST ID_USUARI ÉS PELS CAMBRERS
                cistella = new HashMap<Producte, Integer>();
                comanda = new Comanda(cistella, idUsuari, numTaula);
                return true;
            case R.id.enviarcomanda_menu_empresa_activity:
                comanda.setEstatComanda("ACCEPTADA");
                client = new OkHttpClient();
//                Comanda comanda = new Comanda(cistella);
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                List<String> json_llistat = comanda.getJsonComanda();
                Log.d("onClick_Cistella_LIST", "LLISTA JSON"+ json_llistat);

                int contador = 0;

                for (String json_comanda : json_llistat ) {

                    request(client, json_comanda);
                    Log.d("for OnClick", "CONTADOR:" + contador++);

                }
                comanda.getMapa().clear();
                Toast.makeText(getApplicationContext(), "Comanda enviada", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnitem_empresa_activity_inserirproductes:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_empresa_activity, formulariAfegirProductesFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void request(OkHttpClient client, String json) {
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Log.d("OnClick_Cistella_JSON", "Comanda: "+json);
        Request request_insert = new Request.Builder()
                .url(Constants.URL_COMANDES)
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
//                    Log.d("OkHttp2: ", "onResponse: " + body.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void onClick(View v) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.imgbutton_empresa_activity_prendrecomanda:
                getSupportActionBar().show();
//                fabCistella.setVisibility(View.VISIBLE);
//                fabComanda.setVisibility(View.VISIBLE);
                tvLeyendaComandes.setVisibility(View.VISIBLE);
                tvLeyendaCuina.setVisibility(View.GONE);
                tvLeyendaTaules.setVisibility(View.GONE);
                productesFragment.setProductes(productes);
                fragmentTransaction
                        .replace(R.id.fragment_container_empresa_activity, productesFragment)
                        .addToBackStack(null)
                        .commit();
                break;


            case R.id.imgbutton_empresa_activity_veuretaules:
                getSupportActionBar().hide();
//                fabComanda.setVisibility(View.GONE);
//                fabCistella.setVisibility(View.GONE);
//                fabComanda.startAnimation(fabClose);
//                fabCistella.startAnimation(fabClose);

                tvLeyendaTaules.setVisibility(View.VISIBLE);
                tvLeyendaCuina.setVisibility(View.GONE);
                tvLeyendaComandes.setVisibility(View.GONE);
                fragmentTransaction
                        .replace(R.id.fragment_container_empresa_activity, taulesFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.imgbutton_empresa_activity_cuina:
                getSupportActionBar().hide();
//                fabComanda.setVisibility(View.GONE);
//                fabCistella.setVisibility(View.GONE);
//                fabComanda.startAnimation(fabClose);
//                fabCistella.startAnimation(fabClose);

                tvLeyendaCuina.setVisibility(View.VISIBLE);
                tvLeyendaTaules.setVisibility(View.GONE);
                tvLeyendaComandes.setVisibility(View.GONE);
                cuinaFragment.setComandes(comandes);
                cuinaFragment.setProductes(productes);
                fragmentTransaction
                        .replace(R.id.fragment_container_empresa_activity, cuinaFragment)
                        .addToBackStack(null)
                        .commit();
                break;

        }
    }

}