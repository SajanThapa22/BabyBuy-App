package com.example.babybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.babybuy.Adapter.ItemListAdapter;
import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ItemListActivity extends AppCompatActivity {
    ImageButton backcat;
    Button btnadditem;
    Integer itemcatid;
    SearchView searchView;
    TextView itemname, totalprice, tobuyprice;
    ArrayList<ItemDataModel> itemdata;
    RecyclerView itemrecycle;
    ItemDataModel itemDataModel;
    ItemListAdapter adapter;
    Database database = new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        backcat = findViewById(R.id.backtocat);
        btnadditem = findViewById(R.id.itemactivityaddbtn);
        itemname = findViewById(R.id.itemname);
        totalprice = findViewById(R.id.totalprice);
        tobuyprice = findViewById(R.id.tobuytotalprice);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }

        itemcatid = getIntent().getExtras().getInt("pcid");


        itemDataModel = new ItemDataModel();

        itemdata = database.itemfetchdata(itemcatid);

        itemrecycle = findViewById(R.id.product_recy_view);

        itemrecycle.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(itemrecycle);

        adapter = new ItemListAdapter(this, itemdata);

        itemrecycle.setAdapter(adapter);




        btnadditem.setOnClickListener(v -> {
            Intent intent = new Intent(ItemListActivity.this, ItemAddActivity.class);
            intent.putExtra("pcid", itemcatid);
            startActivity(intent);
        });

        backcat.setOnClickListener(v -> {
            redirecttocategory();
        });



        //calculate price
        priceresult();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }
        //Gesture handler for item List
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Database database = new Database(ItemListActivity.this);
            ArrayList<ItemDataModel> alldataswipe = database.itemfetchdata(itemcatid);
            int itemIdSwipe = alldataswipe.get(position).getIditem();
            int itmemstsswipe = alldataswipe.get(position).getStatusitem();

            switch (direction) {
                case ItemTouchHelper.LEFT:

                    //To Delete item
                    database.deleteitem(itemIdSwipe);
                    itemdata.remove(position);
                    itemrecycle.getAdapter().notifyItemRemoved(position);
                    ArrayList<ItemDataModel> rrecentdata = database.itemfetchdata(itemcatid);
                    ItemListAdapter aadapter = new ItemListAdapter(ItemListActivity.this, rrecentdata);
                    itemrecycle.setAdapter(aadapter);
                    priceresult();
                    break;

                case ItemTouchHelper.RIGHT:
                    if (itmemstsswipe == -1) {
                        database.itempurchased(1, itemIdSwipe);
                        ArrayList<ItemDataModel> recentdata = database.itemfetchdata(itemcatid);
                        //Toast.makeText(ProductListActivity.this, "Item Purchased", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(itemrecycle.getAdapter()).notifyDataSetChanged();

                        itemdata.get(position).setStatusitem(1);
                        priceresult();

                    } else if (itmemstsswipe == 1) {
                        database.itempurchased(-1, itemIdSwipe);
                        ArrayList<ItemDataModel> recentdata = database.itemfetchdata(itemcatid);
                       // Toast.makeText(ProductListActivity.this, "Item unmarked", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(itemrecycle.getAdapter()).notifyDataSetChanged();

                        itemdata.get(position).setStatusitem(-1);
                        priceresult();

                    }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorRed))
                    .addSwipeRightLabel("purchased")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .addSwipeRightActionIcon(R.drawable.ic_purchase)
                    .setSwipeRightActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.greencolor))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    //calculate price
    public void priceresult() {
        try {
            totalprice = findViewById(R.id.totalprice);
            tobuyprice = findViewById(R.id.tobuytotalprice);



            itemdata = database.itemfetchdata(itemcatid);



            Double totalPurchasedPrice = 0.0;
            Double totaltoBuyPrice = 0.0;
            for (int i = 0; i < itemdata.size(); i++) {
                if (itemdata.get(i).getStatusitem() == -1) {
                    totaltoBuyPrice += itemdata.get(i).getPriceitem() * itemdata.get(i).getQuanitem();
                } else {
                    totalPurchasedPrice += itemdata.get(i).getPriceitem() * itemdata.get(i).getQuanitem();
                }
            }
            totalprice.setText(String.valueOf(totalPurchasedPrice));
            tobuyprice.setText(String.valueOf(totaltoBuyPrice));
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void redirecttocategory() {
        startActivity(new Intent(ItemListActivity.this, MainActivity.class));
    }

    public void recyclerupdate(){
        adapter = new ItemListAdapter(this, itemdata);

        //add adapter to view
        itemrecycle.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ItemListActivity.this, MainActivity.class);
        startActivity(intent);
    }
}