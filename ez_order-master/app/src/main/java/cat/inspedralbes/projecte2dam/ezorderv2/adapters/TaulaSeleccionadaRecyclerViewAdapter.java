package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

public class TaulaSeleccionadaRecyclerViewAdapter extends RecyclerView.Adapter<TaulaSeleccionadaRecyclerViewAdapter.ViewHolder> {

    View.OnClickListener listener;
    View view;
    Comanda comanda;
    List<Comanda> comandaCompleta;
    Map<Producte, Integer> cistella;
    String numTaula;
    List<Producte> llistaProductes;
    TaulaSeleccionadaRecyclerViewAdapter.ViewHolder viewHolder;


    public TaulaSeleccionadaRecyclerViewAdapter(Map<Producte, Integer> cistella) {
        this.cistella = cistella;
    }
    public TaulaSeleccionadaRecyclerViewAdapter(Comanda comanda) {
        this.comanda = comanda;
    }

    @NonNull
    @Override
    public TaulaSeleccionadaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("onCreateViewHolder", "Entra TaulaSelecionadaRecyclerViewAdapter");
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_taula_productes, parent, false);
        return new TaulaSeleccionadaRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaulaSeleccionadaRecyclerViewAdapter.ViewHolder holder, int position) {
        this.viewHolder = holder;
        Producte producte = (Producte) cistella.keySet().toArray()[position];
        Log.d("OnBindViewHolder", "Producte: "+ producte);
        int quantitat = (Integer)cistella.values().toArray()[position];
        Log.d("OnBindViewHolder", "Quantitat: "+quantitat);
        float preu = (producte.getPreu()*quantitat);

        String producteString = "  "+producte.getNom();
        String quantitatStringFormat = "  x"+quantitat;
        String preuStringFormat ="  "+(String.format("%.2f", preu) +  " €  ");

        String producteCapitalized = producteString.substring(0, 1).toUpperCase() + producteString.substring(1);


        holder.tvNomProducte.setText(producteCapitalized);
        holder.tvQuantitat.setText(quantitatStringFormat);
        holder.tvPreuUnitat.setText(preuStringFormat);
    }

    @Override
    public int getItemCount() {
        return cistella.size();
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNomProducte;
        TextView tvQuantitat;
        TextView tvPreuUnitat;
        LinearLayout layoutProducte;

        ViewHolder(View itemView) {
            super(itemView);
            //listener = this;
            tvNomProducte = itemView.findViewById(R.id.textView_taula_nomProducte);
            tvQuantitat = itemView.findViewById(R.id.textView_taula_quantitatProducte);
            tvPreuUnitat = itemView.findViewById(R.id.textView_taula_preuProducte);
            layoutProducte = itemView.findViewById(R.id.layout_taula_producte);
            //itemView.setOnClickListener(this);
        }

        public LinearLayout getLayoutProducte() {
            return layoutProducte;
        }

        public TextView getTvNomProducte() {
            return tvNomProducte;
        }

        @Override
        public void onClick(View view) {
            if (listener!=null){
                listener.onClick(view);
            }
        }
    }
}
