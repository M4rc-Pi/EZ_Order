package cat.inspedralbes.projecte2dam.ezorderv2.fragments.empresa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.inspedralbes.projecte2dam.ezorderv2.R;

public class HomeEmpresaFragment extends Fragment {

    public HomeEmpresaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_empresa, container, false);
    }
}