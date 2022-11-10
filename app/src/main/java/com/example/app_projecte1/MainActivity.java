package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        String user = "Test";
        String passwd = "test1234";
        String ip = "192.168.1.1";

        buttonLogIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (!user.equals(userInput.getText().toString()) ) {
                    Log.v("msg","User is incorrect");
                    String text = "User or password is incorrect";
                    message(text);
                }
                if (!passwd.equals(passInput.getText().toString())) {
                    Log.v("msg","The password is incorrect");
                    String text = "User or password is incorrect";
                    message(text);
                }



            }
        });


    }

    public void message(String text) {
        Snackbar sbMsg = Snackbar.make(findViewById(R.id.constraintLayout),text,3000);
        sbMsg.show();
        Log.v("funcion","Msg en la funcion");
    };
}