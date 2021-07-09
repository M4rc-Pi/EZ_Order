package cat.inspedralbes.projecte2dam.ezorderv2.fragments;

import android.os.Bundle;

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
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.CategoriesRecyclerViewAdapter;
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

public class CategoriesFragment extends Fragment {

    public final String TAG = "FRAGMENT CATEGORIES ";
    Map<Producte, Integer> cistella;
    Comanda comanda;
    List<String> categories;
    List<Producte> productesDeLaCategoriaSeleccionada;

    View view;
    RecyclerView recyclerView;
    CategoriesRecyclerViewAdapter adapter;
    OkHttpClient client;
    Parser parser;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        client = new OkHttpClient();
        parser = new Parser();
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recycler_categories, container, false);
        recyclerView = view.findViewById(R.id.recycler_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new CategoriesRecyclerViewAdapter(categories);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request_categories = new Request.Builder()
                        .url(Constants.URL_PRODUCTES_DE_CATEGORIA + categories.get(recyclerView.getChildAdapterPosition(v)))
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
                            Log.d("JSON", "" + json);

                            productesDeLaCategoriaSeleccionada = parser.parsejarProductesCategoriaSeleccionada(json); ///

                            ProductesFragment productesFragment = new ProductesFragment(productesDeLaCategoriaSeleccionada);
                            productesFragment.setComanda(comanda);
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                                    .replace(R.id.fragment_container_main_activity, productesFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setCategories(List<String> categories){
        this.categories = categories;
    }
    public void setComanda(Comanda comanda){
        this.comanda = comanda;
    }
}
