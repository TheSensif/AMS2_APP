package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogIn = findViewById(R.id.buttonLogIn);
        EditText userInput = findViewById(R.id.editUsername);
        EditText passInput = findViewById(R.id.editPassword);
        EditText ipInput = findViewById(R.id.editServerIp);


        String user = "admin";
        String passwd = "1234";

        buttonLogIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Boolean error = false;

                if (!user.equals(userInput.getText().toString()) ) {
                    String text = "User or password is incorrect";
                    error = message(text);
                    userInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

                }
                if (!passwd.equals(passInput.getText().toString())) {
                    String text = "User or password is incorrect";
                    error = message(text);
                    passInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }

                if (ipInput.getText().toString().equals("")) {
                    String text = "Server IP is null!";
                    error = message(text);
                    ipInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }

                if (!error) {
                    Intent newMain = new Intent(MainActivity.this,ieti_industry.class);
                    startActivity(newMain);
                }

            }
        });


    }

    public Boolean message(String text) {
        Snackbar sbMsg = Snackbar.make(findViewById(R.id.constraintLayout),text,3000);
        sbMsg.show();
        return true;
    };
}