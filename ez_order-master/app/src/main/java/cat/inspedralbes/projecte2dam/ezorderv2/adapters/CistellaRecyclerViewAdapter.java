package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import android.icu.text.Edits;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.fragments.CistellaFragment;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

public class CistellaRecyclerViewAdapter extends RecyclerView.Adapter<CistellaRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "--ADAPTER CISTELLA: ";
    int quantitatProducte = 0;
    View view;
    private final Map<Producte, Integer> cistella;
    //public Map<Producte, Integer> productes = new HashMap<>();
    List<Producte> productes;
    public Map<Producte, Integer> sameProducts;
    Comanda comanda;
    TextView tvPreuTotal;
    Integer repetit = 0;
//commit
    FragmentTransaction fragmentTransaction;

    public CistellaRecyclerViewAdapter(Map<Producte, Integer> cistella, Comanda comanda, TextView tvPreuTotal) {
        //this.cistella = cistella;
        this.cistella = comanda.getMapa();
        this.comanda = comanda;
        this.tvPreuTotal = tvPreuTotal;
        productes = new ArrayList<>();
        sameProducts = new HashMap<>();
        getAllCistellaProductes(productes);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CistellaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_cistella, parent, false);
        return new CistellaRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Producte producte = (Producte) cistella.keySet().toArray()[position];

        //Camp nom producte
        holder.tvNomProducte.setText(producte.getNom());

        //Camp quantitat
        for (Map.Entry<Producte, Integer> producteCistella : cistella.entrySet()) {

            if(producteCistella.getKey().getNom().equals(producte.getNom())){
                quantitatProducte = producteCistella.getValue();

                holder.tvQuantitat.setText(String.valueOf("x" + quantitatProducte));
            }

            /*
            //Recórrer la llista dels productes de la cistella
            getAllCistellaProductes(productes);
            for(Producte p : productes) {

                //compara si hi ha el mateix Producte(object) tant a la 'List' com al 'Map'(KEY)
                if (producteCistella.getKey().equals(p)) {

                    //si troba coincidencia, suma el contador de repetit
                    repetit++;

                    for (int i = 0; i < repetit; i++) {
                        //afegeix al 'Map<Producte,Integer> sameProducts' el producte repetit
                        //L'afegeix tantes vegades com hagi trobat que es repeteix
                        sameProducts.put(producteCistella.getKey(), producteCistella.getValue());
                    }
                }

                //variable per a la nova quantitat del producte que es troba al map, que s'haura d'actualitzar
                int novaQuantitat = 0;

                //recorre el 'Map' dels productes repetits, i sumar les seves quantitat en una variable
                for (Map.Entry<Producte, Integer> producteRepetit : sameProducts.entrySet()) {
                    novaQuantitat = novaQuantitat + producteRepetit.getValue();
                }
                cistella.put(producteCistella.getKey(), producteCistella.getValue() + novaQuantitat);
            }
            //TODO: elminar el producte repetit, i afegir el mateix amb la nova quantitat.
            //TODO: COMPROBAR SI EL CODI FUNCIONA
            cistella.remove(producteCistella.getKey());
            */
        }

        //Camp preu
        if(quantitatProducte > 1 ){
            holder.tvPreu.setText(String.format("%.2f", (producte.getPreu())*quantitatProducte) + " €");
        }else {
            holder.tvPreu.setText(String.format("%.2f", producte.getPreu()) + " €");
        }
        //Log.i("CISTELLA", String.valueOf(cistella.get(productes.get(position))));

        //Camp botó (elimina producte)
        holder.imgBtnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //S'elimina de la comanda 1 unitat
                comanda.eliminar(producte, 1);
                //S'elimina del mapa
                eliminarProducteMapa((Producte) cistella.keySet().toArray()[position]);

                if(cistella.get(producte).intValue() == 0) {
                    //S'elimina del viewholder si la quantitat arriba a 0
                    eliminarProducteVista(cistella.keySet().toArray()[position]);
                }

                //Notificacions i canvi en el preu total de la comanda
                notifyDataSetChanged();
                tvPreuTotal.setText(String.format("%.2f", comanda.getPreuTotal())+ " €");
                if(getItemCount() == 0){
                    tvPreuTotal.setText("-");
                }

                Log.i("PRODUCTE ELIMINAT", "Producte '" + producte.getNom()+ "' eliminat de la cistella");
                Toast.makeText(view.getContext(), "Producte eliminat", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cistella.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNomProducte;
        public final TextView tvQuantitat;
        public final TextView tvPreu;
        public final ImageButton imgBtnElimina;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomProducte = itemView.findViewById(R.id.textview_layoutcistella_nom);
            tvQuantitat = itemView.findViewById(R.id.textview_layoutcistella_quantitat);
            tvPreu = itemView.findViewById(R.id.textView_layoutcistella_preu);
            imgBtnElimina = itemView.findViewById(R.id.imageButton_layoutcistella_eliminar);
            imgBtnElimina.setTooltipText("Eliminar");

        }
    }

    public void setItemCount(int valor){

    }
    //encontrar las llaves cistella.get(producte.getNom)
    private void eliminarProducteMapa(Producte producte){

        if(cistella.containsKey(producte.getNom())){
            cistella.remove(producte);
        }
    }

    private void eliminarProducteVista(Object object){

        if(cistella.containsKey(object)){
            cistella.remove(object);
        }
    }

    public void getAllCistellaProductes(List<Producte> productes){

        for (Map.Entry<Producte, Integer> producteCistella : cistella.entrySet()) {
            productes.add(producteCistella.getKey());
        }

        for(Producte producte: productes){
            Log.i("PRODUCTES", producte.getNom());
        }
    }

}
