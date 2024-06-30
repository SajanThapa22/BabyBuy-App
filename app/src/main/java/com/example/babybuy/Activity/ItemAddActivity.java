package com.example.babybuy.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.ByteArrayOutputStream;

public class ItemAddActivity extends AppCompatActivity implements OnMapReadyCallback {



    NetworkInfo networkInfo;
    ConnectivityManager mngr;

    TextView imgfromcam, imgfromgallery;
    Button additembtn;
    ImageView itemaddimage, backicon;
    EditText itemname, additem, itemquan, itemprice;
    final int CAMERA_CODE = 100;
    final int GALLERY_CODE = 200;
    ItemDataModel itemDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        int itemcatidlist = getIntent().getExtras().getInt("pcid");

        //for connecting to database
        Database database = new Database(this);

        //connecting all the required IDs
        additembtn = findViewById(R.id.additembtn);
        itemname = findViewById(R.id.itemtitle);
        itemquan = findViewById(R.id.iditemquantity);
        itemprice = findViewById(R.id.addpriceid);
        additem = findViewById(R.id.itemaddrid);
        itemaddimage = findViewById(R.id.itemimgid);
        imgfromgallery = findViewById(R.id.imgfromgallery);
        imgfromcam = findViewById(R.id.imgfromcam);
        backicon = findViewById(R.id.bgimg);


        //add item image from gallery
        imgfromgallery.setOnClickListener(v -> {
            Intent intgallery = new Intent(Intent.ACTION_PICK);
            intgallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intgallery, GALLERY_CODE);
        });

        //add item image from camera
        imgfromcam.setOnClickListener(v -> {
            Intent intcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intcamera, CAMERA_CODE);
        });

        //for adding item data in item table in the database
        additembtn.setOnClickListener(v -> {

            Integer itemstatus = -1;
            itemDataModel = new ItemDataModel();
            itemDataModel.setImgitem(convertImageViewToByteArray(itemaddimage));
            itemDataModel.setNameitem(itemname.getText().toString());
            itemDataModel.setQuanitem(Integer.parseInt(itemquan.getText().toString()));
            itemDataModel.setPriceitem(Double.parseDouble(itemprice.getText().toString()));
            itemDataModel.setDescripitem(additem.getText().toString());
            itemDataModel.setStatusitem(itemstatus);
            itemDataModel.setIdcatimg(itemcatidlist);

            long result = database.itemadd(itemDataModel);
            if (result == -1) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Succcess", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ItemAddActivity.this, ItemListActivity.class);
                intent.putExtra("pcid", itemcatidlist);
                startActivity(intent);
            }
        });

        //returning back to ItemListActivity
        backicon.setOnClickListener(v -> {
            Intent intent = new Intent(ItemAddActivity.this, ItemListActivity.class);
            intent.putExtra("pcid", itemcatidlist);
            startActivity(intent);
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //for image from camera
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                itemaddimage.setImageBitmap(img);
            } else if (requestCode == GALLERY_CODE) {
                //for image from gallery
                itemaddimage.setImageURI(data.getData());
            }
        }
    }

    byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    //for checking internet connection
    public void checkConnection() {
        mngr = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = mngr.getActiveNetworkInfo();
    }

}