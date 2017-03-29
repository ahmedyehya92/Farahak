package com.line360.loginprojectah.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmed Yehya on 29/03/2017.
 */

public class DateSharedPref {
    SharedPreferences DateSharedPref;
    public DateSharedPref(Context context){
        DateSharedPref = context.getSharedPreferences("DateRef",context.MODE_PRIVATE);    //myRef is name of SharedPreferance
    }
    public void SaveData(String date){
        SharedPreferences.Editor editor = DateSharedPref.edit();
        editor.putString("date",date);       // key and value

        editor.commit();                             // to commit changes (لتنفيذ التغييرارات)

    }

    public String LoadData() {
        String fileContent=DateSharedPref.getString("date","nodate");   // No name will load if no username
        return fileContent;
    }
}
