package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ieti_industry extends AppCompatActivity {



    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ieti_industry);

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