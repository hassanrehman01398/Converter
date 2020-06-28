package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rcarvalho.unitconverter.R;

import java.util.ArrayList;


public class SavedResults extends AppCompatActivity {



    ListView list_view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_results);


        list_view=findViewById(R.id.list_view);


        ArrayList<String> savedResults = SharedPref.getSavedResults(this);


        if(savedResults.size()<1)
        {

            Toast.makeText(this,"No Saved Results To Show",Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,savedResults);


        list_view.setAdapter(adapter);




    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}