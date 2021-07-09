package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.inspedralbes.projecte2dam.ezorderv2.R;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.ComandaProducteQuantitat;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants.EstatComanda;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class CuinaRecyclerViewAdapter extends RecyclerView.Adapter<CuinaRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "--ADAPTER CUINA:";
    View view;
    Comanda comanda;
    List<Integer> platsQueEsPodenEliminar= new ArrayList<Integer>();
    List <ComandaProducteQuantitat> totsElsPlatsDeTotesLesComandes;

    //un constructor que rep el mapa i el transforma en List
    public CuinaRecyclerViewAdapter(List <ComandaProducteQuantitat> totsElsPlatsDeTotesLesComandes) {
        this.totsElsPlatsDeTotesLesComandes = totsElsPlatsDeTotesLesComandes;
    }

    public List<ComandaProducteQuantitat> getTotsElsPlatsDeTotesLesComandes() {
        return totsElsPlatsDeTotesLesComandes;
    }

    public List<Integer> getPlatsQueEsPodenEliminar(){
        return platsQueEsPodenEliminar;
    }

    @NonNull
    @Override
    public CuinaRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_cuina, parent, false);
        return new CuinaRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        comanda = totsElsPlatsDeTotesLesComandes.get(position).getComanda();
        Producte producte = totsElsPlatsDeTotesLesComandes.get(position).getProducte();
        int quantitat = totsElsPlatsDeTotesLesComandes.get(position).getQuantitat();

        if (comanda.getEstatComanda().equals("ACCEPTADA")){
            holder.layout.setBackgroundColor(Color.parseColor("#F1DCBD"));
        }else if (comanda.getEstatComanda().equals("EN_PREPARACIO")){
            holder.layout.setBackgroundColor(Color.rgb(255, 255, 200));
        }else if (comanda.getEstatComanda().equals("CANCELADA")) {
            holder.layout.setBackgroundColor(Color.rgb(255, 200, 200));
            holder.imgButtonFinalitzat.setVisibility(View.INVISIBLE);
            holder.imgButtonCancelar.setVisibility(View.INVISIBLE);
            holder.imgButtonEnProces.setVisibility(View.INVISIBLE);
        }else if (comanda.getEstatComanda().equals("FINALITZADA")) {
            holder.layout.setBackgroundColor(Color.rgb(200, 255, 200));
            holder.imgButtonFinalitzat.setVisibility(View.INVISIBLE);
            holder.imgButtonCancelar.setVisibility(View.INVISIBLE);
            holder.imgButtonEnProces.setVisibility(View.INVISIBLE);
        }

        holder.tvNumComanda.setText(comanda.getCodi());
        holder.tvPlat.setText(producte.getNom());
        holder.tvQuantitat.setText(String.valueOf(quantitat));

        //onClick en button EN_PREPARACIO
        holder.imgButtonEnProces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvPlat.setTextColor(Color.rgb(255, 150, 0));
                holder.tvQuantitat.setTextColor(Color.rgb(255, 150, 0));
                holder.layout.setBackgroundColor(Color.rgb(255, 255, 200));
                int idComanda = totsElsPlatsDeTotesLesComandes.get(position).getComanda().getIdComanda();
                String json = "{\"estat\" : \"" + EstatComanda.EN_PREPARACIO.toString().toUpperCase() + "\"}";
                OkHttpClient client = new OkHttpClient();
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request_insert = new Request.Builder()
                        .url(Constants.URL_COMANDES + idComanda)
                        .put(body)
                        .build();
                try {
                    client.newCall(request_insert).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //onClick en button CANCELADA
        holder.imgButtonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvPlat.setTextColor(Color.rgb(200, 0, 0));
                holder.tvQuantitat.setTextColor(Color.rgb(200, 0, 0));
                holder.layout.setBackgroundColor(Color.rgb(255, 200, 200));
                holder.imgButtonFinalitzat.setVisibility(View.INVISIBLE);
                holder.imgButtonCancelar.setVisibility(View.INVISIBLE);
                holder.imgButtonEnProces.setVisibility(View.INVISIBLE);
                platsQueEsPodenEliminar.add(position);
                int idComanda = totsElsPlatsDeTotesLesComandes.get(position).getComanda().getIdComanda();
                String json = "{\"estat\" : \"" + EstatComanda.CANCELADA.toString().toUpperCase() + "\"}";
                OkHttpClient client = new OkHttpClient();
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request_insert = new Request.Builder()
                        .url(Constants.URL_COMANDES + idComanda)
                        .put(body)
                        .build();
                try {
                    client.newCall(request_insert).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //onClick en button FINALITZADA
        holder.imgButtonFinalitzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvPlat.setTextColor(Color.rgb(0, 150, 0));
                holder.tvQuantitat.setTextColor(Color.rgb(0, 150, 0));
                holder.layout.setBackgroundColor(Color.rgb(200, 255, 200));
                holder.imgButtonFinalitzat.setVisibility(View.INVISIBLE);
                holder.imgButtonCancelar.setVisibility(View.INVISIBLE);
                holder.imgButtonEnProces.setVisibility(View.INVISIBLE);
                platsQueEsPodenEliminar.add(position);
                int idComanda = totsElsPlatsDeTotesLesComandes.get(position).getComanda().getIdComanda();
                String json = "{\"estat\" : \"" + EstatComanda.FINALITZADA.toString().toUpperCase() + "\"}";
                OkHttpClient client = new OkHttpClient();
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, json);
                Request request_insert = new Request.Builder()
                        .url(Constants.URL_COMANDES + idComanda)
                        .put(body)
                        .build();
                try {
                    client.newCall(request_insert).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return totsElsPlatsDeTotesLesComandes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNumComanda;
        public final TextView tvPlat;
        public final TextView tvQuantitat;
        public final ImageButton imgButtonCancelar;
        public final ImageButton imgButtonEnProces;
        public final ImageButton imgButtonFinalitzat;
        public final View layout;

        public ViewHolder(View view) {
            super(view);
            tvNumComanda = view.findViewById(R.id.textview_layout_recycler_cuina_numcomanda);
            tvPlat = view.findViewById(R.id.textview_layout_recycler_cuina_nomproducte);
            tvQuantitat = view.findViewById(R.id.textview_layout_recycler_cuina_quantitat);
            imgButtonCancelar = view.findViewById(R.id.imgbutton_layout_recycler_cuina_cancelar);
            imgButtonEnProces = view.findViewById(R.id.imgbutton_layout_recycler_cuina_enproces);
            imgButtonFinalitzat = view.findViewById(R.id.imgbutton_layout_recycler_cuina_finalitzat);
            layout = view.findViewById(R.id.layout_layoutrecyclercuina_layoutitem);
        }
    }
}

