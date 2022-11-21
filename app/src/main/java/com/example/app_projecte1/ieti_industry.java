package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class ieti_industry extends AppCompatActivity {
    private String user,ip,password;
    private WebSocketClient cc;
    private JSONObject json;
    ArrayList<String> components = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ieti_industry);
        TableLayout mainTable = findViewById(R.id.tableLayout);

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
                    String temp=bytesToObject(message);

                    try {
                        json=new JSONObject(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("infoJson", String.valueOf(json));
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

        // EVENT LISTENER LOGOUT BUTTON
        Button buttonLogout=findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc.close();
            }
        });

        try {
            JSONObject obj = json;
            System.out.println(obj);
            int quantityElements = obj.length();

            List<String> namesList = new ArrayList<>();
            Iterator<String> stringIterator = obj.keys();
            while (stringIterator.hasNext()) {
                namesList.add(stringIterator.next());
            }
            for (int i = 0; i < quantityElements; i++) {
                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(mainTable.getLayoutParams());// TableLayout is the parent view

                TextView textView = new TextView(this);
                textView.setLayoutParams(rowParams);// TableRow is the parent view
                if (namesList.get(i).equals("switch")) {
                    JSONArray arrayData = obj.getJSONArray("switch");
                    System.out.println(arrayData.length());
                    List<String> namesSwitch = new ArrayList<>();

                    for (int j = 0; j < arrayData.length(); j++) {
                        System.out.println(arrayData.get(j));
                        JSONObject switchObj = arrayData.getJSONObject(j);
                        if (j==0) {
                            stringIterator = switchObj.keys();
                            while (stringIterator.hasNext()) {
                                namesSwitch.add(stringIterator.next());
                            }
                        }

                        //tableRow.addView(textView);
                        ToggleButton tb = new ToggleButton(this);
                        tb.setId(Integer.valueOf((String) switchObj.get(namesSwitch.get(0))));
                        if (switchObj.get(namesSwitch.get(2)).equals("on")) {
                            tb.setChecked(true);
                        } else {
                            tb.setChecked(false);
                        }
                        tb.setTextOff("OFF");
                        System.out.println(namesSwitch.get(2));
                        tb.setTextOn((CharSequence) switchObj.get(namesSwitch.get(2)));

                        tableRow.addView(tb);
                        mainTable.addView(tableRow);
                        //Slider sld = new Slider(this,);

                    }
                } else if (namesList.get(i).equals("slider")) {
                    JSONArray arrayData = obj.getJSONArray("slider");
                    List<String> namesSlider = new ArrayList<>();
                    for (int j = 0; j < arrayData.length(); j++) {
                        JSONObject sliderObj = arrayData.getJSONObject(j);
                        if (j == 0) {
                            stringIterator = sliderObj.keys();
                            while (stringIterator.hasNext()) {
                                namesSlider.add(stringIterator.next());
                            }
                        }
                        //tableRow.addView(textView);
                        Slider slid = new Slider(this);

                        slid.setId(Integer.valueOf((String) sliderObj.get(namesSlider.get(0))));

                        System.out.println(sliderObj.get(namesSlider.get(1)));
                        //Getting the value
                        Float val = Float.valueOf((String) sliderObj.get(namesSlider.get(1)));
                        //Initial data
                        slid.setValueFrom(Float.valueOf((String) sliderObj.get(namesSlider.get(2))));
                        //Final data
                        slid.setValueTo(Float.valueOf((String) sliderObj.get(namesSlider.get(3))));
                        //Setting the stepsize and the value of the slider
                        slid.setStepSize(Float.valueOf((String) sliderObj.get(namesSlider.get(4))));
                        slid.setValue(val);

                        tableRow.addView(slid);
                        mainTable.addView(tableRow);
                    }


                }else if (namesList.get(i).equals("dropdown")) {
                    JSONArray arrayData = obj.getJSONArray("dropdown");
                    List<String> nameDropDown = new ArrayList<>();

                    for (int j = 0; j < arrayData.length(); j++) {

                        JSONObject dropdownObj = arrayData.getJSONObject(j);
                        if (j == 0) {
                            stringIterator = dropdownObj.keys();
                            while (stringIterator.hasNext()) {
                                nameDropDown.add(stringIterator.next());
                            }
                        }

                        Spinner dropdown = new Spinner(this);

                        //Getting new data for the spinner
                        JSONObject spinnerLabels = dropdownObj.getJSONObject(nameDropDown.get(3));
                        System.out.println(spinnerLabels);
                        List<String> labelIds = new ArrayList<>();
                        //Get LabelIds
                        stringIterator = spinnerLabels.keys();
                        while (stringIterator.hasNext()) {
                            labelIds.add(stringIterator.next());
                        }

                        System.out.println(labelIds.size());
                        //Get the actual labels to create the array
                        List<String> labelInfo = new ArrayList<>();
                        for (int k = 0; k < labelIds.size(); k++) {
                            labelInfo.add((String) spinnerLabels.get(labelIds.get(k)));
                        }
                        String[] spinnerOpt = labelInfo.toArray(new String[0]);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, spinnerOpt);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        dropdown.setAdapter(adapter);
                        //todo Para conseguir la posicion por defecto, tendiramos que recorrer otra vez el objeto
                        //todo para buscar la posicion exacta en la que se encuentra el objeto y luego indicarle eso al setSelection
                        //dropdown.setSelection();

                        tableRow.addView(dropdown);
                        mainTable.addView(tableRow);
                    }
                }else if (namesList.get(i).equals("sensor")) {
                    JSONArray arrayData = obj.getJSONArray("sensor");
                    List<String> namesSensor = new ArrayList<>();

                    for (int j = 0; j < arrayData.length(); j++) {
                        JSONObject sensorObj = arrayData.getJSONObject(j);
                        if (j == 0) {
                            stringIterator = sensorObj.keys();
                            while (stringIterator.hasNext()) {
                                namesSensor.add(stringIterator.next());
                            }
                        }
                        //tableRow.addView(textView);
                        TextView sensorText = new TextView(this);
                        //Adding basic information to the TextView that will be shown
                        sensorText.setText((CharSequence) sensorObj.get(namesSensor.get(1)));
                        sensorText.setId(Integer.valueOf((String) sensorObj.get(namesSensor.get(0))));
                        sensorText.append("\n"+"Threshhold Low: ");
                        sensorText.append((CharSequence) sensorObj.get(namesSensor.get(2)));
                        sensorText.append("\n"+"Threshold High: ");
                        sensorText.append((CharSequence) sensorObj.get(namesSensor.get(3)));

                        tableRow.addView(sensorText);
                        mainTable.addView(tableRow);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*public String loadJSONFromAsset() {
        String json = "";
        try {
            //We create a InputStream to get the Json file with the components
            InputStream is = getAssets().open("components.json");
            //We will check about its length
            int size = is.available();
            byte[] buffer = new byte[size];
            //We read the data on the buffer
            is.read(buffer);
            is.close();
            //And we will add the buffer data to the String json
            json = new String(buffer,"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;


    }*/
    public String bytesToObject (ByteBuffer arr) {
        String result = "error";
        try {
            // Transforma el ByteButter en byte[]
            byte[] bytesArray = new byte[arr.remaining()];
            arr.get(bytesArray, 0, bytesArray.length);

            // Transforma l'array de bytes en objecte
            ByteArrayInputStream in = new ByteArrayInputStream(bytesArray);
            ObjectInputStream is = new ObjectInputStream(in);
            return (String) is.readObject();

        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (UnsupportedEncodingException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
        return result;
    }



}


