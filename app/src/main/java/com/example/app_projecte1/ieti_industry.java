package com.example.app_projecte1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

public class ieti_industry extends AppCompatActivity {
    private String user,ip,password;
    private WebSocketClient cc;
    private JSONObject json=new JSONObject();
    private boolean messageSended = false;

    private HashMap<String,Slider> componentsSlider=new HashMap<>();
    private HashMap<String,Spinner> componentsDropdown=new HashMap<>();
    private HashMap<String,ToggleButton> componentsSwitch=new HashMap<>();
    private HashMap<String,TextView> componentsSensor=new HashMap<>();
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

                    Log.i("e",message);
                    if(message.equalsIgnoreCase("ERROR")){
                        cc.close();
                    }else if(message.equalsIgnoreCase("OK")){

                        Log.i("i","Taking the OK USER");
                        cc.send("requestConfiguration");

                    } else if(message.equalsIgnoreCase("restart")){

                        Log.i("i","Restart all");
                        messageSended=false;

                    }
                }

                @Override
                public void onMessage(ByteBuffer message){

                    if(messageSended==false){
                        Log.i("i","Taking the configuration");
                        String temp=bytesToObject(message);

                        try {
                            json=new JSONObject(temp);
                            System.out.println(json);
                            messageSended = true;
                            prueba();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            Log.i("infoJson", String.valueOf(json.get("slider")));
                            JSONArray arr = json.getJSONArray("slider");
                            for(int i = 0; i < arr.length(); i++){
                                System.out.println(arr.getJSONObject(i).getString("default"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // TODO put here the value changer ----------------------------------------------------------------------------------
                        String temp=bytesToObject(message);

                        try {
                            json=new JSONObject(temp);
                            System.out.println(json);
                            if(json.getString("component").equalsIgnoreCase("slider")){

                                Slider s =componentsSlider.get(json.getString("id"));

                                s.setValue(Integer.valueOf(json.getString("value")));

                            } else if(json.getString("component").equalsIgnoreCase("switch")){
                                ToggleButton t =componentsSwitch.get(json.getString("id"));
                                t.setChecked(Boolean.valueOf(json.getString("value")));
                            } else if(json.getString("component").equalsIgnoreCase("dropdown")){
                                System.out.println("dropdown");
                                Spinner d =componentsDropdown.get(json.getString("id"));
                                System.out.println(d.getSelectedItem());
                                System.out.println(json.getInt("value"));
                                d.setSelection(json.getInt("value"),true);

                            } else if(json.getString("component").equalsIgnoreCase("sensor")){
                                TextView s =componentsSensor.get(json.getString("id"));
                                s.setText(json.getString("value"));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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



    }
    public void prueba() {
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
                    TableLayout mainTable = findViewById(R.id.tableLayout);

                    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);


                    if (namesList.get(i).equals("switch")) {
                        TableRow tableRow = new TableRow(this);
                        tableRow.setLayoutParams(mainTable.getLayoutParams());// TableLayout is the parent view

                        TextView textView = new TextView(this);
                        textView.setLayoutParams(rowParams);// TableRow is the parent view
                        JSONArray arrayData = obj.getJSONArray("switch");
                        List<String> namesSwitch = new ArrayList<>();
                        for (int j = 0; j < arrayData.length(); j++) {
                            JSONObject switchObj = arrayData.getJSONObject(j);
                            if (j == 0) {
                                stringIterator = switchObj.keys();
                                while (stringIterator.hasNext()) {
                                    namesSwitch.add(stringIterator.next());
                                }
                            }


                            //todo CREAR UN NUEVO COMPONENTE PARA EL MOVIL


                            //tableRow.addView(textView);
                            ToggleButton tb = new ToggleButton(this);
                            //tb.setId((Integer) switchObj.get(namesSwitch.get(2)));
                            tb.setTag(switchObj.get(namesSwitch.get(2)));
                            Boolean activated = true;


                            tb.setTextOff("OFF");
                            //System.out.println(namesSwitch.get(2));
                            tb.setTextOn((CharSequence) switchObj.get(namesSwitch.get(1)));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (switchObj.get(namesSwitch.get(0)) == activated) {
                                            tb.setChecked(true);
                                        } else {
                                            tb.setChecked(false);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    tb.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // TODO TEST MORE AND DO THE SAME WITH OTHER COMPONENTS
                                            try {
                                                switchObj.put("value",tb.isChecked());
                                                switchObj.put("component","switch");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println(switchObj);
                                            Log.i("info","SEND NEW VALUE");
                                            cc.send(jsonToBytes(switchObj));
                                        }
                                    });
                                    try {
                                        componentsSwitch.put(String.valueOf(switchObj.getString("id")),tb);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    tableRow.addView(tb);
                                    mainTable.addView(tableRow);
                                    // Stuff that updates the UI

                                }
                            });


                            //Slider sld = new Slider(this,);

                        }
                    } else if (namesList.get(i).equals("slider")) {
                        TableRow tableRow = new TableRow(this);
                        mainTable.removeView(tableRow);
                        tableRow.setLayoutParams(mainTable.getLayoutParams());// TableLayout is the parent view

                        TextView textView = new TextView(this);
                        textView.setLayoutParams(rowParams);// TableRow is the parent view
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

                            //slid.setId(Integer.valueOf(String.valueOf(sliderObj.get(namesSlider.get(4)))));
                            slid.setTag(String.valueOf(sliderObj.get(namesSlider.get(4))));
                            //Initial data

                            slid.setValueFrom(Float.valueOf(String.valueOf(sliderObj.get(namesSlider.get(1)))));
                            //slid.setValueFrom(0);

                            //Final data
                            slid.setValueTo(Float.valueOf(String.valueOf(sliderObj.get(namesSlider.get(2)))));
                            //slid.setValueTo(10);

                            //Setting the stepsize and the value of the slider
                            slid.setStepSize(Float.valueOf(String.valueOf(sliderObj.get(namesSlider.get(3)))));
                            //slid.setStepSize(0.5F);

                            slid.setValue(Float.valueOf(String.valueOf(sliderObj.get(namesSlider.get(0)))));

                            componentsSlider.put(String.valueOf(sliderObj.getString("id")),slid);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    slid.addOnChangeListener(new Slider.OnChangeListener() {
                                        @Override
                                        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                                            try {
                                                sliderObj.put("value",value);
                                                sliderObj.put("component","slider");
                                                cc.send(jsonToBytes(sliderObj));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                    tableRow.addView(slid);
                                    //tableRow.removeView((View) tableRow.getParent());
                                    //mainTable.removeView((View) mainTable.getParent());
                                    System.out.println(mainTable.getParent());
                                    mainTable.removeView((View) mainTable.getParent());

                                    mainTable.addView(tableRow);
                                    // Stuff that updates the UI

                                }
                            });
                        }


                    } else if (namesList.get(i).equals("dropdown")) {
                        TableRow tableRow = new TableRow(this);
                        tableRow.setLayoutParams(mainTable.getLayoutParams());// TableLayout is the parent view

                        TextView textView = new TextView(this);
                        textView.setLayoutParams(rowParams);// TableRow is the parent view
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
                            JSONArray spinnerLabels = dropdownObj.getJSONArray("values");
                            //dropdown.setId(Integer.valueOf(String.valueOf(dropdownObj.get(nameDropDown.get(2)))));
                            dropdown.setTag(String.valueOf(dropdownObj.get(nameDropDown.get(2))));

                            List<String> labelIds = new ArrayList<>();
                            List<String> labelInfo = new ArrayList<>();

                            //Get LabelIds
                            /*stringIterator = spinnerLabels.getJSONObject("values");
                            while (stringIterator.hasNext()) {
                                labelIds.add(stringIterator.next());
                            }*/

                            for (int k = 0; k < spinnerLabels.length(); k++) {
                                JSONObject label = spinnerLabels.getJSONObject(k);
                                stringIterator = label.keys();
                                while (stringIterator.hasNext()) {
                                    labelIds.add(stringIterator.next());
                                    labelInfo.add((String) label.get((labelIds.get(k))));

                                }
                            }

                            //Get the actual labels to create the array
                            /*for (int k = 0; k < labelIds.size(); k++) {
                                labelInfo.add((String) spinnerLabels.get(labelIds.get(k)));
                            }*/
                            String[] spinnerOpt = labelInfo.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                    android.R.layout.simple_spinner_item, spinnerOpt);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            dropdown.setAdapter(adapter);
                            //todo Para conseguir la posicion por defecto, tendiramos que recorrer otra vez el objeto
                            //todo para buscar la posicion exacta en la que se encuentra el objeto y luego indicarle eso al setSelection
                            //dropdown.setSelection();
                            System.out.println();

                            componentsDropdown.put(String.valueOf(dropdownObj.getString("id")),dropdown);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            try {
                                                dropdownObj.put("component","dropdown");
                                                dropdownObj.put("value",dropdown.getSelectedItemPosition());
                                                cc.send(jsonToBytes(dropdownObj));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    tableRow.addView(dropdown);
                                    mainTable.addView(tableRow);
                                    // Stuff that updates the UI

                                }
                            });
                        }
                    } else if (namesList.get(i).equals("sensor")) {
                        TableRow tableRow = new TableRow(this);
                        tableRow.setLayoutParams(mainTable.getLayoutParams());// TableLayout is the parent view

                        TextView textView = new TextView(this);
                        textView.setLayoutParams(rowParams);// TableRow is the parent view
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
                            sensorText.setText((CharSequence) sensorObj.get(namesSensor.get(2)));
                            //sensorText.setId(Integer.valueOf(String.valueOf(sensorObj.get(namesSensor.get(1)))));
                            sensorText.setTag(String.valueOf(sensorObj.get(namesSensor.get(1))));
                            sensorText.append("\n" + "Threshhold Low: ");
                            sensorText.append(String.valueOf(sensorObj.get(namesSensor.get(3))));
                            sensorText.append("\n" + "Threshold High: ");
                            sensorText.append(String.valueOf(sensorObj.get(namesSensor.get(0))));
                            componentsSensor.put(String.valueOf(sensorObj.getString("id")),sensorText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tableRow.addView(sensorText);
                                    mainTable.addView(tableRow);
                                    // Stuff that updates the UI

                                }
                            });

                        }
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


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
    public static byte[] jsonToBytes (JSONObject obj) {
        byte[] result = null;
        try {
            // Transforma l'objecte a bytes[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj.toString());
            oos.flush();
            result = bos.toByteArray();
        } catch (IOException e) { e.printStackTrace(); }
        return result;
    }



}

// TODO when create a component set onchange listener and send json with id and new value , send via byte buffer
