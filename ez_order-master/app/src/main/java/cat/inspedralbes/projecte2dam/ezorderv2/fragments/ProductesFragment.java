package cat.inspedralbes.projecte2dam.ezorderv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.ProductesRecyclerViewAdapter;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

public class ProductesFragment extends Fragment {

    List<Producte> productes;
    Map<Producte, Integer> cistella;
    Comanda comanda;

    View view;
    RecyclerView recyclerView;

    public ProductesFragment() {
        productes = new ArrayList<Producte>();
    }

    public ProductesFragment(List<Producte> productes) {
        this.productes = productes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recycler_productes, container, false);
        recyclerView = view.findViewById(R.id.recycler_productes);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ProductesRecyclerViewAdapter(productes, comanda));
        recyclerView.setItemViewCacheSize(productes.size());  //soluciona el problema de les quantitats canviant de posició al recycler

        return view;
    }

    public void setProductes (List<Producte> productes){
        this.productes = productes;
    }
    public void setComanda (Comanda comanda){
        this.comanda = comanda;
    }
    public RecyclerView getRecyclerView(){return recyclerView;}
}