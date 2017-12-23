package com.apps.nishtha.quizzer;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nishtha on 23/12/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    Context context;
    ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.single_category,parent,false));
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        if(categoryArrayList!=null){
            holder.tvCategoryName.setText(categoryArrayList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        ImageView ivCategory;
        TextView tvCategoryName;
        CardView cardCategory;
        public CategoryHolder(View itemView) {
            super(itemView);
            ivCategory=itemView.findViewById(R.id.ivCategory);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
            cardCategory=itemView.findViewById(R.id.cardCategory);
        }
    }
}
