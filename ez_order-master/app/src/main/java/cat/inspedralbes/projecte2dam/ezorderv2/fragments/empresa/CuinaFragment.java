package cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.CuinaRecyclerViewAdapter;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.ComandaProducteQuantitat;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Parser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CuinaFragment extends Fragment {

    private final String TAG = "CUINA FRAGMENT:  ";
    List<Comanda> comandes;
    List<ComandaProducteQuantitat> totsElsPlatsDeTotesLesComandes;
    List<Producte> productes;

    private OkHttpClient client;
    private Parser parser;
    RecyclerView recyclerView;
    CuinaRecyclerViewAdapter adapter;
    View view;

    public CuinaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        client = new OkHttpClient();
        parser = new Parser();
        totsElsPlatsDeTotesLesComandes = new ArrayList<ComandaProducteQuantitat>();

        //connecta amb la bd per coneixer els plats que s'han de preparar
        Request request_categories = new Request.Builder()
                .url(Constants.URL_COMANDES + "?order=timestamp")
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
                    String json = responseBody.string();
                    comandes = parser.parsejaComandes(json, productes);

                    for (Comanda c: comandes){
                        HashMap<Producte, Integer> mapa = (HashMap<Producte, Integer>) c.getMapa();
                        for (Map.Entry<Producte, Integer> pair : mapa.entrySet()) {
                            ComandaProducteQuantitat cpq = new ComandaProducteQuantitat(c, pair.getKey(), pair.getValue());
                            if (pair.getKey().getId_categoria() != 1)
                            totsElsPlatsDeTotesLesComandes.add(cpq);
                        }
                    }

                    // Inflate the layout for this fragment
                    view =  inflater.inflate(R.layout.fragment_recycler_cuina, container, false);
                    recyclerView = view.findViewById(R.id.recycler_cuina);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setItemViewCacheSize(totsElsPlatsDeTotesLesComandes.size());
                    recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public boolean onFling(int velocityX, int velocityY) {
                            if (velocityY > 0) {
                                for (int i = 0; i < adapter.getPlatsQueEsPodenEliminar().size(); i++){
                                    adapter.getTotsElsPlatsDeTotesLesComandes().remove(adapter.getPlatsQueEsPodenEliminar().get(i));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            return false;
                        }
                    });
//        adapter = new CuinaRecyclerViewAdapter();

                    // Set the adapter
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        recyclerView = (RecyclerView) view;
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new CuinaRecyclerViewAdapter(totsElsPlatsDeTotesLesComandes);
                        recyclerView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void setComandes(List<Comanda> comandes){this.comandes = comandes;}
    public void setProductes(List<Producte> productes){this.productes = productes;}
    public void refreshComandes(){

    }
}
