package com.example.babybuy.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babybuy.R;
import com.example.babybuy.Database.Database;

public class RegisterActivity extends AppCompatActivity {
    EditText Username, Userlastname, Useremail, Userpassword, Usercpassword;
    Button rdrbtn;
    TextView redirecttologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Database db = new Database(this);

        //For input value
        Username = findViewById(R.id.phn_no);
        Userlastname = findViewById(R.id.item_title);
        Useremail = findViewById(R.id.item_des);
        Userpassword = findViewById(R.id.item_quan);
        Usercpassword = findViewById(R.id.item_price);

        //inorder to change notification color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }


        //to register and redirect to login
        rdrbtn = findViewById(R.id.btn_sms);
        redirecttologin = findViewById(R.id.rsingup);

        //Clicklistner add for register button
        rdrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declaring string for all input fields name, lastname, password, confirm password
                String firstname = Username.getText().toString();
                String lastname = Userlastname.getText().toString();
                String email = Useremail.getText().toString();
                String password = Userpassword.getText().toString();
                String cpassword = Usercpassword.getText().toString();


                //Emtpy value checked
                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter all data", Toast.LENGTH_SHORT).show();
                } else {

                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        //password length checked
                        if (password.length() >= 8) {

                            //password and confirm password checked
                            if (password.equals(cpassword)) {

                                //email already exist or not checked
                                boolean checkuser = db.checkemail(email);

                                if (checkuser == false) {
                                    Toast.makeText(RegisterActivity.this, "user already exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    //insert data to Database
                                    boolean i = db.insert(firstname, lastname, email, password);
                                    // after successfully inserted
                                    if (i == true) {
                                        Toast.makeText(RegisterActivity.this, "successfully update", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }

                                    //incase any problem occured
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //incase password and confirm password
                            else {
                                Usercpassword.setError("confirm password doesn't match");
                                Toast.makeText(RegisterActivity.this, "confirm password doesn't match", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //if password length doesn't match
                        else {
                            Userpassword.setError("minimum 8 length");
                            Toast.makeText(RegisterActivity.this, "Enter 8 digit password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //if user didn't enter valid email address
                    else {
                        Useremail.setError("enter valid email");
                        Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        redirecttologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Redirecting to Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
