package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cat.inspedralbes.projecte2dam.ezorderv2.R;

public class TaulesRecyclerViewAdapter extends RecyclerView.Adapter<TaulesRecyclerViewAdapter.ViewHolder> {


    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    View.OnClickListener listener;

    Context context;

    public TaulesRecyclerViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Infla el layout dels items desde el fitxer xml
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_recycler_taules, parent, false);
        return new ViewHolder(view);
    }

    // Uneix les dades al TextView a cada item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(mData[position]);
        //holder.myTextView.setOnClickListener(listener);
        holder.botoTaula.setOnClickListener(listener);
    }

    // Número total d'items
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // Emmagatzema i recicla les vistes mentre es desplacen des de la pantalla
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageButton botoTaula;

        ViewHolder(View itemView) {
            super(itemView);
            listener = this;
            myTextView = itemView.findViewById(R.id.info_text);
            botoTaula = itemView.findViewById(R.id.imageButton_taula);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getItem(int id) {
        return mData[id];
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // L'activitat pare implementarà aquest mètode per a respondre als clicks dels esdeveniments
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
