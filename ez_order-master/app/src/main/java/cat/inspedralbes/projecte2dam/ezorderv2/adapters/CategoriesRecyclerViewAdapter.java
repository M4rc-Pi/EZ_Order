
package cat.inspedralbes.projecte2dam.ezorderv2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.inspedralbes.projecte2dam.ezorderv2.R;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final String TAG = "--ADAPTER CATEGORIES:";
    View view;
    private View.OnClickListener listener;
    private final List<String> categories;

    public CategoriesRecyclerViewAdapter(List<String> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recicler_categories, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerViewAdapter.ViewHolder holder, int position) {
        String categoria = categories.get(position);
        String categoriaCapitalized = categoria.substring(0, 1).toUpperCase() + categoria.substring(1);
        holder.tvCategoria.setText(categoriaCapitalized);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvCategoria;

        public ViewHolder(View view) {
            super(view);
            tvCategoria = view.findViewById(R.id.textview_layoutcategories_nom);
        }
    }
}
