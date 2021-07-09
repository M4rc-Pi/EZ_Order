package cat.inspedralbes.projecte2dam.ezorderv2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.CistellaRecyclerViewAdapter;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CistellaFragment extends Fragment implements View.OnClickListener {

    Map<Producte, Integer> cistella;
    Comanda comanda;
    View view;
    RecyclerView recyclerView;
    CistellaRecyclerViewAdapter cistellaRecyclerViewAdapter;
    OkHttpClient client;
    String url_comanda = Constants.URL_COMANDES;
    List<String> json_llistat;
    TextView tvPreuTotal;

    public CistellaFragment() {
//        cistella = new HashMap<Producte, Integer>();
//        json_llistat = new ArrayList<>();
    }

    public CistellaFragment(Comanda comanda) {
        this.comanda = comanda;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cistella, container, false);
        tvPreuTotal = view.findViewById(R.id.textview_fragment_cistella_preutotal);
        cistellaRecyclerViewAdapter = new CistellaRecyclerViewAdapter(comanda.getMapa(), comanda, tvPreuTotal);
        recyclerView = view.findViewById(R.id.recycler_fragment_cistella);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(cistellaRecyclerViewAdapter);
        cistellaRecyclerViewAdapter.notifyDataSetChanged();
        Button myButton = (Button) view.findViewById(R.id.button_fragment_cistella_enviar);
        myButton.setOnClickListener((View.OnClickListener) this);

        //Preu total de la comanda
        showPreuTotalComanda();

        return view;
    }

    public void setComanda(Comanda comanda){
        this.comanda = comanda;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_fragment_cistella_enviar:

                comanda.setEstatComanda("ACCEPTADA");
                Log.d("OnClick_Cistella", "Boto Enviar Apretat");

                client = new OkHttpClient();
//                Comanda comanda = new Comanda(cistella);

                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");

                json_llistat = comanda.getJsonComanda();
                Log.d("onClick_Cistella_LIST", "LLISTA JSON"+ json_llistat);

                int contador = 0;

                for (String json_comanda : json_llistat ) {

                    request(url_comanda, client, json_comanda);
                    Log.d("for OnClick", "CONTADOR:" + contador++);

                }
                comanda.getMapa().clear();
                Log.i("MAPA COMANDA", comanda.getMapa().toString());
                Toast.makeText(view.getContext(), "Comanda feta", Toast.LENGTH_SHORT).show();
                    /*
                    RequestBody body = RequestBody.create(JSON, json_comanda);
                    Log.d("OnClick_Cistella_JSON", "Comanda: "+json_comanda);

                    Request request_insert = new Request.Builder()
                            .url(url_comanda)
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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                     */
                }
        }


    public void request(String url, OkHttpClient client, String json) {

        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);
        Log.d("OnClick_Cistella_JSON", "Comanda: "+json);

        Request request_insert = new Request.Builder()
                .url(url_comanda)
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        cistellaRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void showPreuTotalComanda() {

        TextView tvPreuTotal = view.findViewById(R.id.textview_fragment_cistella_preutotal);
        tvPreuTotal.setText("  "+String.format("%.2f", comanda.getPreuTotal()) + " €");
        if (recyclerView.getAdapter().getItemCount() == 0) {
            tvPreuTotal.setText("-");
        }
    }

    public void setPreuTotalComanda(Comanda comanda, float preu){

        TextView tvPreuTotal = view.findViewById(R.id.textview_fragment_cistella_preutotal);
        tvPreuTotal.setText("  "+String.format("%.2f", comanda.getPreuTotal() - preu) + " €");
    }

}