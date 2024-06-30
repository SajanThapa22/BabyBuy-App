package com.example.babybuy.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.babybuy.Database.Database;
import com.example.babybuy.DataModels.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    EditText smbl, s_title, s_descrip, s_quan, s_cost, s_locate;
    Button btnsms;
    ImageView bgimg;
    Integer pid;
    Database database = new Database(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }

        smbl = findViewById(R.id.phn_no);
        s_title = findViewById(R.id.item_title);
        s_descrip = findViewById(R.id.item_des);
        s_quan = findViewById(R.id.item_quan);
        s_cost = findViewById(R.id.item_price);
        btnsms = findViewById(R.id.btn_sms);
        s_locate = findViewById(R.id.item_location);
        bgimg = findViewById(R.id.smsbgimg);

        try {
            pid = getIntent().getExtras().getInt("productid");
        } catch (Exception ex) {
            pid = -1; //Or some error status //
        }

        if (pid != -1) {
            settext();
        }


        //call SendMessage method
        btnsms.setOnClickListener(v -> {
            SendMessage();

        });

        bgimg.setOnClickListener(view -> {
            startActivity(new Intent(MessageActivity.this, MainActivity.class));

        });

    }


    //SMS send method
    public void SendMessage() {
        String stringPhone = smbl.getText().toString().trim();
        String stringtitle = s_title.getText().toString().trim();
        String stringdes = s_descrip.getText().toString().trim();
        String stringquantity = s_quan.getText().toString().trim();
        String stringprice = s_cost.getText().toString().trim();
        String fullmessage = "title: " + stringtitle + " Description: " + stringdes + " quantity: " + stringquantity +
                " price: " + stringprice;


        if (stringPhone.equals("") || stringtitle.equals("") || stringquantity.equals("") || stringprice.equals("")) {
            Toast.makeText(this, "enter all field", Toast.LENGTH_SHORT).show();
        } else {
            SmsManager smsman = SmsManager.getDefault();
            smsman.sendTextMessage(stringPhone, null, fullmessage, null, null);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            smbl.getText().clear();
            s_title.getText().clear();
            s_descrip.getText().clear();
            s_quan.getText().clear();
            s_cost.getText().clear();
        }
    }

    public void settext() {
        ArrayList<ItemDataModel> pdm = database.itemfetchdataformapload(pid);
        Toast.makeText(this, "SMS Section", Toast.LENGTH_SHORT).show();
        s_title.setText(pdm.get(0).getNameitem());
        s_descrip.setText(pdm.get(0).getDescripitem());
        s_quan.setText(String.valueOf(pdm.get(0).getQuanitem()));
        s_cost.setText(String.valueOf(pdm.get(0).getPriceitem()));
        s_locate.setText(pdm.get(0).getAddressitem());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
        startActivity(intent);
    }


}