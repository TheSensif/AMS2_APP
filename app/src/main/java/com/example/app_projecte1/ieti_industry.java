package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ieti_industry extends AppCompatActivity {



    
    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ieti_industry);
        TableLayout mainTable = findViewById(R.id.tableLayout);
        ArrayList<String> components = new ArrayList<>();
        /*ArrayList<String> arraySliders = new ArrayList<>();
        ArrayList<String> arraySwitch = new ArrayList<>();
        ArrayList<String> arrayDropDown = new ArrayList<>();
        ArrayList<String> arraySensor = new ArrayList<>();*/


        /*for (int i = 0; i < components.size(); i++) {
            if (components.get())
        }*/
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
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

                        //todo CREAR UN NUEVO COMPONENTE PARA EL MOVIL


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
                        /*if (tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow);
                        }
                        if (mainTable.getParent() != null) {
                            ((ViewGroup)mainTable.getParent()).removeView(mainTable);
                        }*/
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
                        /*if (tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow);
                        }
                        if (mainTable.getParent() != null) {
                            ((ViewGroup)mainTable.getParent()).removeView(mainTable);
                        }*/
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
                        /*if (tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow);
                        }
                        if (mainTable.getParent() != null) {
                            ((ViewGroup)mainTable.getParent()).removeView(mainTable);
                        }*/
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
                        /*if (tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow);
                        }
                        if (mainTable.getParent() != null) {
                            ((ViewGroup)mainTable.getParent()).removeView(mainTable);
                        }*/
                        tableRow.addView(sensorText);
                        mainTable.addView(tableRow);

                    }
                }
                        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

        public String loadJSONFromAsset() {
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
    }
}
