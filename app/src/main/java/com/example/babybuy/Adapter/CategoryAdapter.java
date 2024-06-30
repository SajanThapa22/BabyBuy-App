package com.example.babybuy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Activity.ItemListActivity;
import com.example.babybuy.R;
import com.example.babybuy.DataModels.CatDataModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<CatDataModel> catarraymodel;

    public CategoryAdapter(Context context, ArrayList<CatDataModel> catarraymodel) {
        this.context = context;
        this.catarraymodel = catarraymodel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cat_name;
        ImageView cat_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.catname);
            cat_img = itemView.findViewById(R.id.imgcat);

        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CatDataModel catmodel = catarraymodel.get(position);
        holder.cat_name.setText(catmodel.getNamecat());
        if(catmodel.getImgcat() != null & catmodel.getImgcat().length > 0){
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(catmodel.getImgcat(), 0, catmodel.getImgcat().length);
        holder.cat_img.setImageBitmap(ImageDataInBitmap);}
        holder.cat_img.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemListActivity.class);
            intent.putExtra("pcid", catmodel.getIdcat());
            context.startActivity(intent);
        });
        holder.cat_name.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemListActivity.class);
            intent.putExtra("pcid", catmodel.getIdcat());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return catarraymodel.size();
    }


}
