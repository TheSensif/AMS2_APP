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

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private WebSocketClient cc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogIn = findViewById(R.id.buttonLogIn);
        EditText userInput = findViewById(R.id.editUsername);
        EditText passInput = findViewById(R.id.editPassword);
        EditText ipInput = findViewById(R.id.editServerIp);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messagePop(extras.getString("data"));
        }


        buttonLogIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Boolean error = false;
                String text="";
                if ((userInput.getText().toString()).length()>0 && (passInput.getText().toString()).length()>0 && (ipInput.getText().toString()).length()>0 ) {
                    String location="ws://";
                    location+=ipInput.getText().toString();
                    location+=":8888";

                    try {

                        String finalLocation = location;
                        cc = new WebSocketClient(new URI(finalLocation), (Draft) new Draft_6455()) {
                            @Override
                            public void onMessage(String message) {
                                if(message.equalsIgnoreCase("OK")){
                                    Intent newMain = new Intent(MainActivity.this,ieti_industry.class);
                                    newMain.putExtra("user",userInput.getText().toString());
                                    newMain.putExtra("password",passInput.getText().toString());
                                    newMain.putExtra("ip", finalLocation);
                                    startActivity(newMain);
                                    cc.close();

                                }else if(message.equalsIgnoreCase("ERROR")){
                                    messagePop("Credenciales incorrectas (user/password)");
                                    cc.close();
                                }

                            }

                            @Override
                            public void onOpen(ServerHandshake handshake) {
                                messagePop("CONECTADO");
                                cc.send(userInput.getText().toString()+"&"+passInput.getText().toString());



                            }

                            @Override
                            public void onClose(int code, String reason, boolean remote) {
                                ipInput.setEnabled(true);
                                //messagePop("Cerrando!");
                            }

                            @Override
                            public void onError(Exception ex) {
                                ipInput.setEnabled(true);
                                //messagePop(" "+ex);
                                Log.i("i",""+ex);

                            }
                        };
                        cc.connect();
                    }catch (java.net.URISyntaxException e){
                        error=true;
                        messagePop("ERROR CONEX");
                    }
                    //si falla:
                     text = "User or password is incorrect";
                    userInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    passInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }else{
                    //POP UP de rellene
                     text = "Todos los campos son obligatorios";
                     error=true;
                    //message(text);
                }

                if (error) {


                    messagePop(text);

                }

            }
        });


    }

    public Boolean messagePop(String text) {
        Snackbar sbMsg = Snackbar.make(findViewById(R.id.constraintLayout),text,3000);
        sbMsg.show();
        return true;
    };




}
