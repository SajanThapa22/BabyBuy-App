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
import com.example.babybuy.Model.CatDataModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<CatDataModel> catarraymodel;

    public CategoryAdapter(Context context, ArrayList<CatDataModel> catarraymodel) {
        this.context = context;
        this.catarraymodel = catarraymodel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ccatname;
        ImageView ccatimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ccatname = itemView.findViewById(R.id.designcatname);
            ccatimage = itemView.findViewById(R.id.ccatimage);

        }
    }

    //oncreate view method
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view generate, from is method, path define, group, use false to scroll
        View v = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CatDataModel catmodel = catarraymodel.get(position);
        holder.ccatname.setText(catmodel.getCatname());
        if(catmodel.getCatimage() != null & catmodel.getCatimage().length > 0){
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(catmodel.getCatimage(), 0, catmodel.getCatimage().length);
        holder.ccatimage.setImageBitmap(ImageDataInBitmap);}
        holder.ccatimage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemListActivity.class);
            intent.putExtra("pcid", catmodel.getCatid());
            context.startActivity(intent);
        });
        holder.ccatname.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemListActivity.class);
            intent.putExtra("pcid", catmodel.getCatid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return catarraymodel.size();
    }


}
