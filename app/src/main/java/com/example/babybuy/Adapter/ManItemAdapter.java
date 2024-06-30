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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Activity.GoogleMapAct;
import com.example.babybuy.Activity.UpdateItemAct;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Activity.MessageActivity;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;

public class ManItemAdapter extends RecyclerView.Adapter<ManItemAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemDataModel> arrayList;
    ItemDataModel pdm;


    public ManItemAdapter(Context context, ArrayList<ItemDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemtitle, itemdes, itemprice, itemquan;
        TextView item_stats_box;
        ImageView itemedit, itemimg, itemmap, itemsendsms;
        CardView itemcard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemtitle = itemView.findViewById(R.id.itemidtitle);
            itemdes = itemView.findViewById(R.id.itemiddesc);
            itemprice = itemView.findViewById(R.id.itemidprice);
            itemquan = itemView.findViewById(R.id.itemquanid);
            item_stats_box = itemView.findViewById(R.id.statpurchaseditem);
            itemimg = itemView.findViewById(R.id.itemidimg);
            itemedit = itemView.findViewById(R.id.itemlistedit);
            itemcard = itemView.findViewById(R.id.viewcardid);
            itemmap = itemView.findViewById(R.id.mapitemlist);
            itemsendsms = itemView.findViewById(R.id.itemsms);


        }
    }

    @NonNull
    @Override
    public ManItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        ManItemAdapter.ViewHolder viewHolder = new ManItemAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Database db = new Database(context);
        ItemDataModel pdm = arrayList.get(position);

        holder.itemtitle.setText(pdm.getNameitem());
        holder.itemdes.setText(pdm.getDescripitem());
        holder.itemprice.setText(String.valueOf(pdm.getPriceitem()));
        holder.itemquan.setText(String.valueOf(pdm.getQuanitem()));
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(pdm.getImgitem(), 0, pdm.getImgitem().length);
        holder.itemimg.setImageBitmap(ImageDataInBitmap);

        if (pdm.getStatusitem() < 0) {
            holder.item_stats_box.setText("No");
        } else {
            holder.item_stats_box.setText("Yes");
        }

        holder.itemmap.setOnClickListener(view -> {
            Intent intmap = new Intent(context, GoogleMapAct.class);
            intmap.putExtra("productid", pdm.getIditem());
            context.startActivity(intmap);
        });

        holder.itemsendsms.setOnClickListener(view -> {
            Intent imap = new Intent(context, MessageActivity.class);
            imap.putExtra("productid", pdm.getIditem());
            context.startActivity(imap);
        });



        holder.itemedit.setOnClickListener(view -> {
            Intent intmap = new Intent(context, UpdateItemAct.class);
            intmap.putExtra("productid", pdm.getIdcatimg());
            context.startActivity(intmap);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
