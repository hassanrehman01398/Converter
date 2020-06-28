package com.example.converter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SharedPref
{




    public  static  void SaveInPref(Context ac,String  val)
    {
        SharedPreferences sharedPreferences = ac.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int totalSavedNumbers = getTotalSavedNumbers(ac);
        setTotalSavedResults(ac,totalSavedNumbers+1);
        editor.putString("Val"+totalSavedNumbers,val);
        editor.apply();

    }
    public  static  ArrayList<String> getSavedResults(Context ac)
    {
      SharedPreferences sharedPreferences = ac.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);

      ArrayList<String> savedResults=new ArrayList<>();

      for(int i=0;i<getTotalSavedNumbers(ac);i++)
      {
         savedResults.add( sharedPreferences.getString("Val"+i,null));
      }



      return savedResults;
    }

    private   static  void setTotalSavedResults(Context ac,int  val)
    {
        SharedPreferences sharedPreferences = ac.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SavedVal",val);
        editor.apply();

    }
    private   static  int getTotalSavedNumbers(Context ac)
    {
        SharedPreferences sharedPreferences = ac.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        return   sharedPreferences.getInt("SavedVal",-1);
    }






}
