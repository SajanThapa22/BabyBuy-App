package com.example.babybuy.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babybuy.Database.Database;
import com.example.babybuy.R;

public class LoginActivity extends AppCompatActivity {
    EditText Useremail, Userpassword;
    Button Btnlogin, Btnsignup;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Database db = new Database(this);
        Useremail = findViewById(R.id.enteremail);
        Userpassword = findViewById(R.id.enterpassword);
        Btnlogin = findViewById(R.id.btnlog);
        Btnsignup = findViewById(R.id.btnsignup);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.greencolor));
        }
        //setting onClickListener on login button
        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Converted edittext to string
                email = Useremail.getText().toString();
                password = Userpassword.getText().toString();


                //for checking null value in input in email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter username and Password", Toast.LENGTH_SHORT).show();
                }

                //email and password check
                else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {


                    boolean i = db.checkemailandpassword(email, password);
                    if (i == true) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent ilogin = new Intent(LoginActivity.this, MainActivity.class);
                        // ilogin.putExtra("Email", email);
                        getemail();
                        startActivity(ilogin);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                    }
                }


                //if email is incorrect, showing error message
                else {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email ", Toast.LENGTH_LONG).show();
                    Useremail.setError(" Valid email is required");
                }
            }
        });
        //setting onClickListener on signup button
        Btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    public void getemail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("email", email);
        Ed.apply();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

}