package com.example.babybuy.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.babybuy.Adapter.ManItemAdapter;
import com.example.babybuy.Adapter.ItemListAdapter;
import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ManItemFragment extends Fragment {

    public ManItemFragment() {
        // Required empty public constructor
    }

    int productsts;
    Database database;
    ArrayList<ItemDataModel> alldatapurchased;
    ArrayList<ItemDataModel> itemdata;
    RecyclerView item_recycler;
    ManItemAdapter adapter;
    ManItemAdapter manadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.items_list, container, false);
        Button btnpurchased = view.findViewById(R.id.itempurchased);
        Button btnbuy = view.findViewById(R.id.notpurchased);
        database = new Database(getActivity());
        item_recycler = view.findViewById(R.id.itemrecycler);
        item_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // show purchased item when clicked
        itemdata = database.itemfetchdataforpurchased(1);
        manadapter = new ManItemAdapter(getActivity(), itemdata);
        item_recycler.setAdapter(manadapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(item_recycler);

        btnpurchased.setOnClickListener(v -> {
            itemdata = database.itemfetchdataforpurchased(1);
            manadapter = new ManItemAdapter(getActivity(), itemdata);
            item_recycler.setAdapter(manadapter);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(item_recycler);
        });


        //show unpurchased item when clicked
        btnbuy.setOnClickListener(v -> {
            itemdata = database.itemfetchdataforpurchased(-1);
            adapter = new ManItemAdapter(getActivity(), itemdata);
            item_recycler.setAdapter(adapter);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(item_recycler);
        });
        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }


        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Database db = new Database(getActivity());
            ArrayList<ItemDataModel> alldataswipe = db.itemfetchdataformap();
            int productIdswipe = alldataswipe.get(position).getIditem();
            int productstsswipe = alldataswipe.get(position).getStatusitem();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    db.deleteitem(productIdswipe);
                    alldataswipe.remove(position);
                    item_recycler.getAdapter().notifyItemRemoved(position);
                    ArrayList<ItemDataModel> rrecentdata = db.itemfetchdataforpurchased(1);
                    ItemListAdapter aadapter = new ItemListAdapter(getActivity(), rrecentdata);
                    item_recycler.setAdapter(aadapter);
                    Toast.makeText(getActivity(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    if (productstsswipe == -1) {
                        db.itempurchased(1, productIdswipe);
                        itemdata = db.itemfetchdataforpurchased(1);
                        ManItemAdapter aadapter2 = new ManItemAdapter(getActivity(), itemdata);
                        item_recycler.setAdapter(aadapter2);
                        //Toast.makeText(ProductListActivity.this, "Item Purchased", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(item_recycler.getAdapter()).notifyDataSetChanged();

                        itemdata.get(position).setStatusitem(1);


                    } else if (productstsswipe == 1) {
                        db.itempurchased(-1, productIdswipe);
                        itemdata = db.itemfetchdataforpurchased(-1);
                        ManItemAdapter aadapter2 = new ManItemAdapter(getActivity(), itemdata);
                        item_recycler.setAdapter(aadapter2);
                        // Toast.makeText(ProductListActivity.this, "Item unmarked", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(item_recycler.getAdapter()).notifyDataSetChanged();

                        itemdata.get(position).setStatusitem(-1);


                    }
                    break;

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorRed))
                    .addSwipeRightLabel("purchased or tobuy")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .addSwipeRightActionIcon(R.drawable.ic_purchase)
                    .setSwipeRightActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.greencolor))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}