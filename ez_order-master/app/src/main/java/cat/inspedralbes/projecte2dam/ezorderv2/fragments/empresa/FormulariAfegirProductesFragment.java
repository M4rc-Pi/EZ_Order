package cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cat.inspedralbes.projecte2dam.ezorderv2.CarregarImatgeActivity;
import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants.URL_CATEGORIES;
import static cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants.URL_TOTS_ELS_PRODUCTES;

public class FormulariAfegirProductesFragment extends Fragment {

    View view;
    Spinner spCategories;
    List<String> totesLesCategories;
    ArrayAdapter<String> spinnerAdapter;
    EditText etNom;
    EditText etDescripcio;
    EditText etPreu;
    CheckBox chAlergens;
    CheckBox chPicant;
    CheckBox chVeggie;
    Button btnCarregarImg;
    Button btnGuardar;

    OkHttpClient client;
    String json;
    Parser parser;

    Producte producte;

    public FormulariAfegirProductesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_formulari_afegir_productes, container, false);

        spCategories = view.findViewById(R.id.spinner_formulari_productes_spinnercategories);
        client = new OkHttpClient();
        parser = new Parser();
        producte = new Producte();
        // s'inicien tots a 0, en cas de que el checkBox isChecked, el canviem (veure l'onClick del botó guardar)
        producte.setAlergens(0);
        producte.setPicant(0);
        producte.setVeggie(0);

        //el producte necessita coneixer la seva categoria,
        Request request_categories = new Request.Builder()
                .url(URL_CATEGORIES)
                .build();

        client.newCall(request_categories).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("SPINNER", "onFailure: FAILED");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                getActivity().runOnUiThread(() -> {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        json = responseBody.string();
                        totesLesCategories = new ArrayList<String>();
                        totesLesCategories = parser.parsejaCategories(json);
                        spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, totesLesCategories);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spCategories.setAdapter(spinnerAdapter);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        etNom = view.findViewById(R.id.edittext_formulari_productes_nomproducte);
        etDescripcio = view.findViewById(R.id.edittext_formulari_productes_descripcioproducte);
        etPreu = view.findViewById(R.id.edittext_formulari_productes_preuproducte);
        chAlergens = view.findViewById(R.id.checkbox_formulari_productes_alergens);
        chPicant = view.findViewById(R.id.checkbox_formulari_productes_picant);
        chVeggie = view.findViewById(R.id.checkbox_formulari_productes_veggie);

        btnCarregarImg = view.findViewById(R.id.button_formulari_productes_btncarregarimatge);
        btnCarregarImg.setOnClickListener(this::onClickCarregaImg);
        btnGuardar = view.findViewById(R.id.button_formulari_productes_btnguardarproducte);
        btnGuardar.setOnClickListener(this::onClickCarregaImg);
        return view;
    }


    public void onClickCarregaImg(View v) {
        switch (v.getId()) {

            case R.id.button_formulari_productes_btncarregarimatge:
                String nom = etNom.getText().toString().toLowerCase();
                producte.setNom(nom);
                if (nom.isEmpty()){
                    Toast.makeText(getContext(), "INDICA UN NOM DE PRODUCTE ABANS DE PUJAR UNA FOTO", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(view.getContext(), CarregarImatgeActivity.class);
                    intent.putExtra("producte", (Serializable) producte);
                    startActivity(intent);
                }
                break;
            case R.id.button_formulari_productes_btnguardarproducte:
                String descripcio = etDescripcio.getText().toString();
                Float preu = Float.parseFloat(etPreu.getText().toString());
                producte.setId_categoria(spCategories.getSelectedItemPosition()+1);
                producte.setDescripcio(descripcio);
                producte.setPreu(preu);
                //agafar la url de la imatge i passarla a sharedPref
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", 0);
                String strFoto = sharedPreferences.getString("foto", "");
                producte.setFoto(strFoto);
                if (chAlergens.isChecked()) producte.setAlergens(1);
                if (chPicant.isChecked()) producte.setPicant(1);
                if (chVeggie.isChecked())   producte.setVeggie(1);

                json = parser.getJSONProducte(producte);
                Log.d("***Carregar Producte", "-----onClickGuardaProducte: " + json);

                //pujar el producte a la BD
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request_insert = new Request.Builder()
                        .url(URL_TOTS_ELS_PRODUCTES)
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
                            Log.d("OkHttp2: ", "PRODUCTE PUJAT A LA BD: " + body.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }


    }
}