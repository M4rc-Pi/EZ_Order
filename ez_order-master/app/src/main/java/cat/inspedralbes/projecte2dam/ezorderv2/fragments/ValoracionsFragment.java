package cat.inspedralbes.projecte2dam.ezorderv2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import cat.inspedralbes.projecte2dam.ezorderv2.R;

public class ValoracionsFragment extends Fragment {


    View view;
    RatingBar ratingBar;
    EditText etPuntuacio;
    EditText etComentari;

    String puntuacioString;
    String comentari;

    float puntuacioMitjana;
    int countValoracio;
    float puntuacio;


    public ValoracionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_valoracions, container, false);
        ratingBar = view.findViewById(R.id.ratingBar_puntuacions);
        etPuntuacio = view.findViewById(R.id.editText_puntuacio);
        etComentari = view.findViewById(R.id.editText_comentari);

        //AGAFAR PUNTUACIO ACTUAL I NUMERO DE VEGADES QUE S'HA PUNTUAT DE LA BD AMB OKHTTP I GUARDAR-HO A LA VARIABLE "puntuacioMitjana" I "countValoracio"
        puntuacioMitjana = 4.2f;
        countValoracio = 4;

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("onClick Valorar", "Estrelles seleccionades "+ rating);
                //puntuacio = ratingBar.getRating();
                puntuacioMitjana = (puntuacioMitjana + rating)/countValoracio;
                puntuacioString = String.format("%.1f", puntuacioMitjana);
                etPuntuacio.setText(" "+puntuacioString);
            }
        });


        return view;
    }

    public void onClickPublicar (View view) {

        countValoracio++;
        comentari = etComentari.getText().toString();

        //FER INSERT A LA BD AMB OKHTTP DE LA PUNTUACIO A LA TAULA VALORACIONS

        Toast.makeText(view.getContext(), "Gràcies per la teva valoració!", Toast.LENGTH_SHORT).show();

        //OKHTTP

    }
}