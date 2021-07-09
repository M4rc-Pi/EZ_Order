package cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.RegistreActivity;
import cat.inspedralbes.projecte2dam.ezorderv2.adapters.TaulesRecyclerViewAdapter;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.ProductesFragment;


public class TaulesFragment extends Fragment implements TaulesRecyclerViewAdapter.ItemClickListener{

    TaulesRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    View view;
    String[] data;

    FragmentTransaction fragmentTransaction;

    public TaulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        data = new String[]{"  Taula 1", "  Taula 2", "  Taula 3", "  Taula 4",
                "  Taula 5", "  Taula 6", "  Taula 7", "  Taula 8",
                "  Taula 9", " Taula 10", " Taula 11", " Taula 12",
                " Taula 13", " Taula 14", " Taula 15"};

        // set up the RecyclerView
        view = inflater.inflate(R.layout.fragment_taules, container, false);
        recyclerView = view.findViewById(R.id.recycler_taules);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        adapter = new TaulesRecyclerViewAdapter(view.getContext(), data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "Has apretat la taula " +data[position]
                + " Posició: "+ adapter.getItem(position)
                + ", que està a la posició de l'Array " + position);

//        TaulaSeleccionadaComandesFragment taulaSeleccionadaComandesFragment = new TaulaSeleccionadaComandesFragment(data[position], position);
//        getParentFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container_empresa_activity, taulaSeleccionadaComandesFragment)
//                .addToBackStack(null)
//                .commit();

        TaulaSeleccionadaFragment taulaSeleccionadaFragment = new TaulaSeleccionadaFragment(data[position], position);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_empresa_activity, taulaSeleccionadaFragment, "TAULA_SELECCIONADA_FRAGMENT")
                .addToBackStack(null)
                .commit();
    }
}