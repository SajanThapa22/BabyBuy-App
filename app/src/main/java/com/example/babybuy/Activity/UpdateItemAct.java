package com.example.babybuy.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateItemAct extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment smfrag;
    FusedLocationProviderClient client;
    GoogleMap map_location;
    NetworkInfo networkInfo;
    ConnectivityManager mngr;
    List<Address> address;
    ArrayList<ItemDataModel> itemdm;
    Database database;
    Geocoder geocoder;
    TextView itemimgcam, itemimggallery;
    Button btnitemupdate;
    ImageView itemaddimg, iconback;
    EditText item_name, item_address, item_quan, item_price;
    final int CAMERA_CODE = 100;
    final int GALLERY_CODE = 200;
    Double lat, lng;
    ItemDataModel itemDataModel;
    Integer id, category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        id = getIntent().getExtras().getInt("productid");
        category_id = getIntent().getExtras().getInt("pcid");
        database = new Database(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }

        //connecting all the required IDs
        btnitemupdate = findViewById(R.id.productupdatebtnid);
        item_name = findViewById(R.id.productupdatetitleid);
        item_quan = findViewById(R.id.productupdatequantityid);
        item_address = findViewById(R.id.productuodatedesid);
        item_price = findViewById(R.id.productupdatepriceid);
        itemaddimg = findViewById(R.id.itemupdateimgid);
        itemimggallery = findViewById(R.id.productupdatefromgallery);
        itemimgcam = findViewById(R.id.productupdatefromcamera);
        iconback = findViewById(R.id.upbgimg);


        //select image from gallery
        itemimggallery.setOnClickListener(v -> {
            Intent intgallery = new Intent(Intent.ACTION_PICK);
            intgallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intgallery, GALLERY_CODE);
        });

        //select image from camera
        itemimgcam.setOnClickListener(v -> {
            Intent intcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intcamera, CAMERA_CODE);
        });
        smfrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
        smfrag.getMapAsync(this);

        btnitemupdate.setOnClickListener(v -> {
            ItemAddActivity adp = new ItemAddActivity();
            itemDataModel = new ItemDataModel();
            itemDataModel.setImgitem(adp.convertImageViewToByteArray(itemaddimg));
            itemDataModel.setNameitem(item_name.getText().toString());
            itemDataModel.setQuanitem(Integer.parseInt(item_quan.getText().toString()));
            itemDataModel.setPriceitem(Double.parseDouble(item_price.getText().toString()));
            itemDataModel.setDescripitem(item_address.getText().toString());
            itemDataModel.setStatusitem(itemdm.get(0).getStatusitem());
            itemDataModel.setIdcatimg(itemdm.get(0).getIdcatimg());
            itemDataModel.setLatitem(lat);
            itemDataModel.setLongitem(lng);

            int result = database.updateitem(itemDataModel, itemdm.get(0).getIditem());
            if (result == -1) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Succcess", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateItemAct.this, ItemListActivity.class);
                intent.putExtra("pcid", itemdm.get(0).getIdcatimg());
                startActivity(intent);
            }
        });

        //redirect to ItemListActivity
        iconback.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateItemAct.this, ItemListActivity.class);
            intent.putExtra("pcid", itemdm.get(0).getIdcatimg());
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //for camera
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                itemaddimg.setImageBitmap(img);
            } else if (requestCode == GALLERY_CODE) {
                //for gallery
                itemaddimg.setImageURI(data.getData());
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map_location = googleMap;
        itemdm = database.itemfetchdataformapload(id);

        item_name.setText(itemdm.get(0).getNameitem());
        item_quan.setText(String.valueOf(itemdm.get(0).getQuanitem()));
        item_price.setText(String.valueOf(itemdm.get(0).getPriceitem()));
        item_address.setText(itemdm.get(0).getDescripitem());
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(itemdm.get(0).getImgitem(), 0, itemdm.get(0).getImgitem().length);
        itemaddimg.setImageBitmap(ImageDataInBitmap);

        checkConnection();
        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
            lat = itemdm.get(0).getLatitem();
            lng = itemdm.get(0).getLongitem();
            try {
                getitemlocation(lat, lng);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please Check Network", Toast.LENGTH_SHORT).show();
        }

        map_location.setOnMapClickListener(lating -> {
            map_location.clear();

            checkConnection();
            if (networkInfo.isConnected() && networkInfo.isAvailable()) {

                lat = lating.latitude;
                lng = lating.longitude;
                try {
                    getitemlocation(lat, lng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please Check Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for checking internet connection
    public void checkConnection() {
        mngr = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = mngr.getActiveNetworkInfo();
    }


    public void getitemlocation(Double mlat, Double mlng) throws IOException {
        geocoder = new Geocoder(UpdateItemAct.this, Locale.getDefault());
        if (mlat != 0) {
            address = geocoder.getFromLocation(mlat, mlng, 1);
            if (address != null) {
                String mAddress = address.get(0).getAddressLine(0);
                String city = address.get(0).getLocality();
                String state = address.get(0).getAdminArea();
                String Country = address.get(0).getCountryName();
                String postalCode = address.get(0).getPostalCode();
                String knownName = address.get(0).getFeatureName();

                String productaddress = itemdm.get(0).getNameitem() + " " + mAddress;


                if (mAddress != null) {
                    MarkerOptions mmarkerOptions = new MarkerOptions();
                    LatLng lating = new LatLng(mlat, mlng);
                    mmarkerOptions.position(lating).title(productaddress);

                    map_location.addMarker(mmarkerOptions).showInfoWindow();
                    map_location.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(lating, 17));


                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateItemAct.this, ItemListActivity.class);
        intent.putExtra("pcid", itemdm.get(0).getIdcatimg());
        startActivity(intent);
    }
}