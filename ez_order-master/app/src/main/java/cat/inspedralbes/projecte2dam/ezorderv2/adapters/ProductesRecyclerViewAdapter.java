package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

public class ProductesRecyclerViewAdapter extends RecyclerView.Adapter<ProductesRecyclerViewAdapter.ViewHolder> {

    View view;
    private final List<Producte> productes;
    public List<Producte> productesCistella;
    //    private Map<Producte, Integer> cistella = new HashMap<Producte, Integer>();
    private Map<Producte, Integer> cistella;
    Comanda comanda;

    public ProductesRecyclerViewAdapter(List<Producte> productes, Comanda comanda) {
        this.productes = productes;
        this.comanda = comanda;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recicler_productes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductesRecyclerViewAdapter.ViewHolder holder, int position) {
        Producte producte = productes.get(position);
        String nom = producte.getNom().toLowerCase();
        String descripcio = producte.getDescripcio();
        float preu = producte.getPreu();
        int alergen = producte.isAlergens();
        int picant = producte.isPicant();
        int veggie = producte.isVeggie();

        if (nom.equals("frigo pie") || nom.equals("frigo pie"))
        Log.d("PRODUCTE.GETFOTO()", "onBindViewHolder:  " + producte.getFoto());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ez-order-projecte.appspot.com/img/" + producte.getNom().replaceAll(" ", "").toLowerCase());
        for (Producte p : productes) {
            //if (!p.getFoto().equals("null"))
            if (p.getFoto() != null)
            Log.d("VISTA PRODUCTES", "onBindViewHolder: " + producte.getFoto());
        }

        final long SIZE = 1024*1024;
        storageRef.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap foto = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgViewFoto.setImageBitmap(foto);
            }
        });
        holder.tvNom.setText(nom);
        holder.tvDescripcio.setText(descripcio);
        holder.tvPreu.setText(String.format("%.2f", preu) +  " €");

        //Controlar els camps que no cal pintar
        if (descripcio.equals("null"))
            holder.tvDescripcio.setVisibility(View.GONE);
        //
        if (alergen == 0)
            holder.imgViewAllergen.setVisibility(View.GONE);
        else  holder.imgViewAllergen.setVisibility(View.VISIBLE);

        if (picant == 0)
            holder.imgViewPicant.setVisibility(View.GONE);
        else holder.imgViewPicant.setVisibility(View.VISIBLE);

        if (veggie == 0)
            holder.imgViewVeggie.setVisibility(View.GONE);
        else holder.imgViewVeggie.setVisibility(View.VISIBLE);

        holder.btnSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantitat = Integer.parseInt(holder.etQuantitat.getText().toString());
                quantitat += 1;
                holder.etQuantitat.setText(String.valueOf(quantitat));
                holder.btnOK.setEnabled(true);
            }
        });

        holder.btnRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantitat = Integer.parseInt(holder.etQuantitat.getText().toString());

                if (quantitat > 0) {
                    quantitat -= 1;
                    holder.etQuantitat.setText(String.valueOf(quantitat));
                } else{
                    holder.btnRestar.setEnabled(false);
                    Toast.makeText(view.getContext(), "La quantitat no pot ser negativa", Toast.LENGTH_SHORT).show();
                }
                holder.btnRestar.setEnabled(true);
            }
        });

        holder.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //comprova si hi ha quantitat
                String quantitatProducte = holder.etQuantitat.getText().toString();

                if (quantitatProducte.equals("0")) {

                    Toast.makeText(view.getContext(), "No hi ha res seleccionat", Toast.LENGTH_SHORT).show();
                    holder.btnOK.setEnabled(false);

                } else {
                    holder.btnOK.setEnabled(true);
                    //comprova si es num negatiu
                    String quantitatStr = holder.etQuantitat.getText().toString();
                    char negatiu = quantitatStr.charAt(0);
                    int quantitatInt = 0;
                    if (negatiu == '-') {
                        quantitatInt = Integer.parseInt(quantitatStr);
//                    Log.d("-------Productes RV", "onClick: quantitatINT::: " + quantitatInt);
                    } else {
                        quantitatInt += Integer.parseInt(quantitatStr);
                    }
                    cistella = comanda.getMapa();

                    //comprova si el producte ja hi era a la cistella
                    if (cistella.get(productes.get(position)) != null) {
                        int total = cistella.get(productes.get(position)).intValue();
                        cistella.remove(productes.get(position));
                        cistella.put(productes.get(position), total + quantitatInt);
                        if (total <= 0) {
                            cistella.remove(productes.get(position));
                        }
                    } else {
//                    Producte copiat = (Producte) cistella.keySet().toArray()[position];
                        cistella.remove(productes.get(position));
                        cistella.put(productes.get(position), quantitatInt);
//                    Log.d("TAGGGGGGGGGGGGGGGGGG", "onClick: " + cistella.toString());
                    }

                    holder.etQuantitat.setText("0");
                    Log.d("ADAPTER PRODUCTES: ", "onClickOk: " + cistella.toString());
                    Toast.makeText(view.getContext(), "Afegit", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return productes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNom;
        public final TextView tvDescripcio;
        public final TextView tvPreu;
        public final ImageView imgViewAllergen;
        public final ImageView imgViewPicant;
        public final ImageView imgViewVeggie;
        public final EditText etQuantitat;
        public final ImageView imgViewFoto;

        ImageButton btnSumar;
        ImageButton btnRestar;
        ImageButton btnOK;

        public ViewHolder (@NonNull View view){
            super(view);
            tvNom = view.findViewById(R.id.textview_layoutproductes_nom);
            tvDescripcio = view.findViewById(R.id.textview_layoutproductes_descripcio);
            tvPreu = view.findViewById(R.id.textview_layoutproductes_preu);
            etQuantitat = view.findViewById(R.id.edittext_layoutproductes_quantitat);
            imgViewAllergen = view.findViewById(R.id.imgview_layoutproductes_alergen);
            imgViewPicant = view.findViewById(R.id.imgview_layoutproductes_picant);
            imgViewVeggie = view.findViewById(R.id.imgview_layoutproductes_veggie);
            imgViewFoto = view.findViewById(R.id.imageview_layout_productes_foto);

            btnSumar = view.findViewById(R.id.button_layoutproductes_sumar);
            btnRestar = view.findViewById(R.id.button_layoutproductes_restar);
            btnSumar = view.findViewById(R.id.button_layoutproductes_sumar);
            btnOK = view.findViewById(R.id.button_layoutproductes_ok);


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

    public boolean existeixProducte(Producte producte)
    {
        boolean existe = false;

        for(int i=0;i<productes.size();i++)
        {
            if(productes.get(i).getNom().equals(producte.getNom()));
            {
                existe = true;
                break;
            }
        }
        return existe;
    }
}

