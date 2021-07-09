package cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa;

import android.graphics.Color;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.TaulaSeleccionadaRecyclerViewAdapter;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class TaulaSeleccionadaFragment extends Fragment {

    TaulaSeleccionadaRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    View view;

    OkHttpClient client;
    String url_productesTaula;
    Parser parser;

    List<Comanda> comandaCompleta;
    Comanda comanda;
    Map<Producte, Integer> cistella;
    Map<Producte, String> estatsProductes;
    List<String> estatsProductesString;
    String numTaula;
    int idTaula;
    float preuTotalComanda;
    String estatComanda;
    String codiComanda;
    String codiComandaAnterior;

    int currentComanda;
    int contador;

    boolean requestFinalitzada = false;

    Button butonNext;

    public TaulaSeleccionadaFragment() {
        // Required empty public constructor
    }

    public TaulaSeleccionadaFragment(String numTaula, int posicioTaula) {
        this.numTaula = numTaula;
        idTaula = posicioTaula + 1;
        preuTotalComanda = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

            client = new OkHttpClient();
            url_productesTaula = Constants.URL_COMANDES + "?join=PRODUCTES&join=USUARIS&filter=taula,eq," + idTaula;
            parser = new Parser();
            comandaCompleta = new ArrayList<>();
            cistella = new HashMap<>();
            estatsProductes = new HashMap<>();
            estatsProductesString = new ArrayList<>();
            currentComanda = 0;

            view = inflater.inflate(R.layout.fragment_taula_seleccionada, container, false);
            recyclerView = view.findViewById(R.id.recycler_taula_productes_comanda);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        butonNext = (Button) view.findViewById(R.id.button_next_comanda);
//        butonNext.setOnClickListener(this);


            requestProductesTaula(url_productesTaula);

//        while (!requestFinalitzada) {
//            Log.d("RequestFinalitzada","Esperant a obtenir les dades dels productes da la Taula: "+idTaula + " Request: "+requestFinalitzada);
//            if (requestFinalitzada){
//                break;
//            }
//        }

            return view;
        }

        public void requestProductesTaula (String url){

            Log.d("requestProductesTaula", "ENTRA " + url);

            Request request_categories = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request_categories).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                    Log.d("onCreateView", "onFailure: FAILED");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    Log.d("onResponse", "ENTRA " + url);

                    getActivity().runOnUiThread(() -> {

                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            String json = responseBody.string();
                            Log.d("JSON", "" + json);

                            comandaCompleta = parser.parsejaComandaJoinUsuariProducte(json);

                            TextView tvNumTaula = view.findViewById(R.id.textView_taula_num);
                            TextView tvCodiTaula = view.findViewById(R.id.textView_taula_codi);
                            TextView tvPreuTotal = view.findViewById(R.id.textView_taula_preu);
                            TextView tvNomUsuari = view.findViewById(R.id.textView_taula_nomUsuari); //S'ha de fer algo per aconseguir el nom de l'usuari a través de l'id
                            TextView tvEstatComanda = view.findViewById(R.id.textView_taula_estatComanda);
                            ;

                            tvNumTaula.setText(" "+numTaula);

                            //RECORRER L'ARRAYLIST DE COMANDES I ANAR AFEGINT LES DADES A LA VISTA

                            comanda = comandaCompleta.get(currentComanda);
                            tvCodiTaula.setText(" "+comanda.getCodi());
                            String nomUsuari = comanda.getNomUsuari();
                            String nomUsuariCap = nomUsuari.substring(0, 1).toUpperCase() + nomUsuari.substring(1);
                            tvNomUsuari.setText("   "+nomUsuariCap);
                            tvPreuTotal.setText(" " + String.format("%.2f", comanda.getPreuTotal()) + " € ");

                            estatsProductes = comanda.getEstatsProductes();
                            //estatsProductesString = (List<String>) estatsProductes.values();

                            for (int i = 0; i < estatsProductes.size(); i++) {

                                if (!estatsProductes.values().toArray()[0].equals(estatsProductes.values().toArray()[i])) {
                                    estatComanda = "EN PREPARACIO";
                                    tvEstatComanda.setTextColor(Color.YELLOW);
                                } else {
                                    if (estatsProductes.values().toArray()[i].equals("ACCEPTADA")) {
                                        estatComanda = Constants.EstatComanda.ACCEPTADA.toString();
                                        tvEstatComanda.setTextColor(Color.CYAN);
                                    } else if (estatsProductes.values().toArray()[i].equals("CANCELADA")) {
                                        estatComanda = Constants.EstatComanda.CANCELADA.toString();
                                        tvEstatComanda.setTextColor(Color.RED);
                                    } else if (estatsProductes.values().toArray()[i].equals("FINALITZADA")) {
                                        estatComanda = Constants.EstatComanda.FINALITZADA.toString();
                                        tvEstatComanda.setTextColor(Color.GREEN);
                                    } else {
                                        estatComanda = Constants.EstatComanda.ENVIADA.toString();
                                        tvEstatComanda.setTextColor(Color.WHITE);
                                    }
                                }
                            }

                            tvEstatComanda.setText(" "+estatComanda);

//                        for ( Comanda comanda : comandaCompleta) {
//
//                            codiComandaAnterior = codiComanda;
//                            codiComanda = comanda.getCodi();
//
//                            if (!codiComanda.equals(codiComandaAnterior)) {
//                                tvCodiTaula.setText(comanda.getCodi());
//                                tvNomUsuari.setText(comanda.getNomUsuari());
//                                tvEstatComanda.setText(comanda.getEstatComandaString());
//
//                                Map<Producte, Integer> map = comanda.getMapa();
//                                for ( Map.Entry<Producte,Integer> entry : map.entrySet()) {
//                                    Producte producte = entry.getKey();
//                                    int quantitat = entry.getValue();
//                                    cistella.put(producte, quantitat);
//                                    Log.d("BucleFor", "Producte: " + producte.getNom());
//                                    Log.d("BucleFor", "Quantitat: " + quantitat);
//                                }
//                                preuTotalComanda += comanda.getPreuTotal();
//                            }
//                        }
//
//                        tvPreuTotal.setText(" "+String.format("%.2f", preuTotalComanda) +  " €");

                            adapter = new TaulaSeleccionadaRecyclerViewAdapter(comanda.getMapa());
                            Log.d("OnResponseAdapter", "Adapter: " + adapter);
                            Log.d("OnResponseCistella", "Cistella:" + comanda.getMapa());
                            recyclerView.setAdapter(adapter);
                            Log.d("OnResponseRecycler", "Recycler: " + recyclerView);
                            //adapter.notifyDataSetChanged();

                            requestFinalitzada = true;
                            Log.d("OnResponse", "RequestFinalitzada: " + requestFinalitzada);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }

}


//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        currentComanda++;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.button_next_comanda:
//
//
////                TaulaSeleccionadaFragment taulaSeleccionadaFragment = new TaulaSeleccionadaFragment(numTaula, idTaula - 1, currentComanda);
////                getParentFragmentManager()
////                        .beginTransaction()
////                        .replace(R.id.fragment_container_empresa_activity, taulaSeleccionadaFragment)
////                        .addToBackStack(null)
////                        .commit();
//
//                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("TAULA_SELECCIONADA_FRAGMENT");
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.detach(currentFragment);
//                fragmentTransaction.attach(currentFragment);
//                fragmentTransaction.commit();
//                break;
//        }
//    }

//        TaulaSeleccionadaComandesFragment taulaSeleccionadaComandesFragment = new TaulaSeleccionadaComandesFragment(data[position], position);
//        getParentFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container_empresa_activity, taulaSeleccionadaComandesFragment)
//                .addToBackStack(null)
//                .commit();


