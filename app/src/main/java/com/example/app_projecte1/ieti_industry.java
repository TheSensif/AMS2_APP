package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class ieti_industry extends AppCompatActivity {
    private String user,ip,password;
    private WebSocketClient cc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ieti_industry);
        //get the values passed :
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             user = extras.getString("user");
             password = extras.getString("password");
             ip = extras.getString("ip");
        }
        // connection block:
        try {


            cc = new WebSocketClient(new URI(ip), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    // TODO Put the json translator
                    Log.i("e",message);
                    if(message.equalsIgnoreCase("ERROR")){
                        cc.close();
                    }else if(message.equalsIgnoreCase("OK")){

                        Log.i("i","Taking the OK USER");
                        cc.send("requestConfiguration");

                    }
                }

                @Override
                public void onMessage(ByteBuffer message){
                    Log.i("i","Taking the configuration");
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    cc.send(user+"&"+password);
                    Log.i("i","OPEN");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Intent newMain = new Intent(ieti_industry.this,MainActivity.class);
                    newMain.putExtra("data","CONNECTION ERROR");
                    startActivity(newMain);
                }

                @Override
                public void onError(Exception ex) {


                }
            };
            cc.connect();
        }catch (java.net.URISyntaxException e){
            Intent newMain = new Intent(ieti_industry.this,MainActivity.class);
            newMain.putExtra("data","CONNECTION ERROR");
            startActivity(newMain);
        }

    }



}