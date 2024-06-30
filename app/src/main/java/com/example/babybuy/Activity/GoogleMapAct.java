package com.example.babybuy.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class GoogleMapAct extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment smf;
    FusedLocationProviderClient client;
    GoogleMap location_map;
    Geocoder geocoder;
    Double lat, lng, latitem, longimg;
    NetworkInfo networkInfo;
    ConnectivityManager mngr;
    List<Address> adrs;
    String addressselected, newitem = "";
    Database database;
    int itemcatid;
    ArrayList<ItemDataModel> itemDataModels;
    ImageView backimg;
    String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        //change notification color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }


        itemcatid = getIntent().getExtras().getInt("productid");
        item = getIntent().getExtras().getString("productname");



        smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        client = LocationServices
                .getFusedLocationProviderClient(this);



        smf.getMapAsync(this);


        backimg = findViewById(R.id.mbgimg);
        backimg.setOnClickListener(view -> {
            startActivity(new Intent(GoogleMapAct.this, MainActivity.class));
        });

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        location_map = googleMap;
        database = new Database(GoogleMapAct.this);
        itemDataModels = database.itemfetchdataformapload(itemcatid);
        latitem = itemDataModels.get(0).getLatitem();
        longimg = itemDataModels.get(0).getLongitem();
        if (longimg != 0.0) {
            try {
                GetAddress(latitem, longimg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void checkConnection() {
        mngr = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = mngr.getActiveNetworkInfo();
    }


    private void GetAddress(double mlat, double mlng) throws IOException {
        geocoder = new Geocoder(GoogleMapAct.this, Locale.getDefault());
        if (mlat != 0) {
            adrs = geocoder.getFromLocation(mlat, mlng, 1);
            if (adrs != null) {
                String mAddress = adrs.get(0).getAddressLine(0);
                String city = adrs.get(0).getLocality();
                String state = adrs.get(0).getAdminArea();
                String Country = adrs.get(0).getCountryName();
                String postalCode = adrs.get(0).getPostalCode();
                String knownName = adrs.get(0).getFeatureName();

                String productaddress = newitem + " " + mAddress;

                addressselected = mAddress;

                if (mAddress != null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng lating = new LatLng(mlat, mlng);
                    markerOptions.position(lating).title(productaddress);
                    location_map.addMarker(markerOptions).showInfoWindow();
                    location_map.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(lating, 17));

                    itemcatid = -1;

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
        Intent intent = new Intent(GoogleMapAct.this, MainActivity.class);
        startActivity(intent);
    }


}