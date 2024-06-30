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
import com.example.babybuy.Activity.MessageActivity;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemDataModel> arrayList;
    ItemDataModel itemdm;


    public ItemListAdapter(Context context, ArrayList<ItemDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_des, item_price, item_quan;
        TextView item_stat_box;
        ImageView itemedit, itemdelete, itemimage, itemlocate, item_sendsms;
        CardView itemcard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.itemidtitle);
            item_des = itemView.findViewById(R.id.itemiddesc);
            item_price = itemView.findViewById(R.id.itemidprice);
            item_quan = itemView.findViewById(R.id.itemquanid);
            item_stat_box = itemView.findViewById(R.id.statpurchaseditem);
            itemimage = itemView.findViewById(R.id.itemidimg);
            itemedit = itemView.findViewById(R.id.itemlistedit);
            itemcard = itemView.findViewById(R.id.viewcardid);
            itemlocate = itemView.findViewById(R.id.mapitemlist);
            item_sendsms = itemView.findViewById(R.id.itemsms);

        }
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        ItemListAdapter.ViewHolder viewHolder = new ItemListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemdm = arrayList.get(position);


        holder.item_title.setText(itemdm.getNameitem());
        holder.item_des.setText(itemdm.getDescripitem());
        holder.item_price.setText(String.valueOf(itemdm.getPriceitem()));
        holder.item_quan.setText(String.valueOf(itemdm.getQuanitem()));
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(itemdm.getImgitem(), 0, itemdm.getImgitem().length);
        holder.itemimage.setImageBitmap(ImageDataInBitmap);

        if (itemdm.getStatusitem() < 0) {
            holder.item_stat_box.setText("No");
        } else {
            holder.item_stat_box.setText("Yes");
        }

        holder.itemlocate.setOnClickListener(view -> {
            Intent intmap = new Intent(context, GoogleMapAct.class);
            intmap.putExtra("productid", itemdm.getIditem());
            intmap.putExtra("productname", itemdm.getNameitem());
            context.startActivity(intmap);
        });

        holder.item_sendsms.setOnClickListener(view -> {
            Intent imap = new Intent(context, MessageActivity.class);
            imap.putExtra("productid", itemdm.getIditem());
            context.startActivity(imap);
        });


        holder.itemedit.setOnClickListener(view -> {
            Intent iedit = new Intent(context, UpdateItemAct.class);
            iedit.putExtra("productid", itemdm.getIditem());
            iedit.putExtra("pcid", itemdm.getIdcatimg());
            context.startActivity(iedit);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
