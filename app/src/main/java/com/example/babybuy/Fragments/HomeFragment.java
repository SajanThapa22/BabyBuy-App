package com.example.babybuy.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Adapter.CategoryAdapter;
import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.CatDataModel;
import com.example.babybuy.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    //Variable for Recyclerview and Adapter
    RecyclerView recycler_cat;
    CategoryAdapter category_adapter;
    ArrayList<CatDataModel> cat_data;
    final int CAMERA_CODE = 100;
    final int GALLERY_CODE = 200;
    TextView catimgcamera, catimggallery;
    ImageView catimg;
    ImageSlider catmainimg;
    FloatingActionButton add_cat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_list, container, false);

        add_cat = view.findViewById(R.id.btnaddcatego);



        //for adding new category
        add_cat.setOnClickListener(v -> {
            addnewcategorydailoge();
        });


        recycler_cat = view.findViewById(R.id.recy_view_cat);
        recycler_cat.setHasFixedSize(false);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

        recycler_cat.setLayoutManager(grid);

        category_adapter = new CategoryAdapter(getContext(), catdata());
        recycler_cat.setAdapter(category_adapter);

        return view;
    }

    private ArrayList<CatDataModel> catdata() {
        Database catdb = new Database(getActivity());
        cat_data = catdb.categoryfetchdata();
        return cat_data;
    }
    private void addnewcategorydailoge() {
        AlertDialog.Builder addcat = new AlertDialog.Builder(getActivity(), R.style.YourThemeName);

        View viewalert = LayoutInflater.from(getActivity()).inflate(R.layout.add_category, null);
        catimggallery = viewalert.findViewById(R.id.imgcatgallery);
        catimgcamera = viewalert.findViewById(R.id.imgcatcam);
        catimg = viewalert.findViewById(R.id.categoimgid);
        addcat.setCancelable(true);
        addcat.setView(viewalert);
        EditText newcategoryname = viewalert.findViewById(R.id.catnamedit);

        //choose item image from gallery
        catimggallery.setOnClickListener(v -> {
            Intent intgallery = new Intent(Intent.ACTION_PICK);
            intgallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intgallery, GALLERY_CODE);
        });

        //choose item image from camera
        catimgcamera.setOnClickListener(v -> {
            Intent intcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intcamera, CAMERA_CODE);
        });

        //add new category button
        addcat.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database db_cat = new Database(getActivity());
                long insertCat = db_cat.categoryadd(newcategoryname.getText().toString(), convertImageViewToByteArray(catimg));
                if (insertCat == -1) {
                    Toast.makeText(getActivity(), "Failed to Update", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    // Collections.reverse(allcatdata);
                    CategoryAdapter adapter = new CategoryAdapter(getContext(), catdata());
                    recycler_cat.setAdapter(adapter);
                }
            }
        });
        addcat.setNegativeButton("Cancel", null);
        addcat.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //for camera
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                catimg.setImageBitmap(img);
            } else if (requestCode == GALLERY_CODE) {
                //for gallery
                catimg.setImageURI(data.getData());
            }
        }
    }


    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}