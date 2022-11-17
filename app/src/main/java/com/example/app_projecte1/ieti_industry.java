package com.example.app_projecte1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;

import java.util.ArrayList;

public class ieti_industry extends AppCompatActivity {


    String texto = "{0=DataModule{switch, id='0', defaul='on', name='Label'}, " +
            "1=DataModule{slider, id='1, defaul='4.5', min=0, max=10, step='0.5'}, " +
            "2=DataModule{dropdown', id='2', defaul='3', value={2=Label 2, 3=Label 3, 5=Label 5, 6=Label 4}}, " +
            "3=DataModule{etiqueta='sensor', id='3', units='ºC', thresholdlow=5, thresholdhigh=10, name='Label'}}";

    
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
    }
}