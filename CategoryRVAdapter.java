package com.example.samachar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {

    final private ArrayList<CategoryRVModal> categoryRVModals;
    final private Context context;
    final private CategoryClickInterface categoryClickInterface;

    public CategoryRVAdapter(ArrayList<CategoryRVModal> categoryRVModals, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVModals = categoryRVModals;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_rv_item, parent, false);
        return new CategoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryRVAdapter.ViewHolder holder, int position) {
        CategoryRVModal categoryRVModal = categoryRVModals.get(position);
        holder.categoryTV.setText(categoryRVModal.getCategory());
        Picasso.get().load(categoryRVModal.getCategoryImageUrl()).into(holder.categoryIV);
    }

    @Override
    public int getItemCount() {
        return categoryRVModals.size();
    }

    public interface CategoryClickInterface {
        void onCategoryClick(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView categoryTV;
        final private ImageView categoryIV;

        public ViewHolder(View itemView) {
            super(itemView);

            categoryIV = itemView.findViewById(R.id.idIVCategory);
            categoryTV = itemView.findViewById(R.id.idTVCategory);

            itemView.setOnClickListener(v -> categoryClickInterface.onCategoryClick(getAdapterPosition()));
        }
    }
}
