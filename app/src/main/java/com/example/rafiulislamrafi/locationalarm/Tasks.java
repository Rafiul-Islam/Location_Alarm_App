package com.example.rafiulislamrafi.locationalarm;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Tasks extends Activity {

    Button showdata;
    DataBase_Helper myDatabase;
    TextView all_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //Hide all the titlebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDatabase = new DataBase_Helper(this);

        all_data = (TextView) findViewById(R.id.text);

        showdata = (Button) findViewById(R.id.viewData);

        viewAllData();
    }

    public void viewAllData() {

        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor data = myDatabase.getAllData();

                if (data.getCount() == 0) {

                    //Show Message
                    //showMessage("Error", "Nothing Found");
                    showMessage("Nothing Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();

                while (data.moveToNext()) {

                    buffer.append("ID : " + data.getString(0) + " \n ");
                    buffer.append("\tLOCATION : " + data.getString(1) + " \n ");
                    buffer.append("\tTASK : " + data.getString(2) + " \n ");
                    buffer.append("\tTIME : " + data.getString(3) + " \n");
                    buffer.append("\tDATE : " + data.getString(4) + " \n\n");
                    buffer.append("------------------------------\n\n");
                }

                //Show all data
                //showMessage("Data", buffer.toString());

                showMessage(buffer.toString());
            }
        });

    }

    /*public void showMessage(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/

    public void showMessage(String allData){

        all_data.setText(""+allData+"\n\n");

    }
}
