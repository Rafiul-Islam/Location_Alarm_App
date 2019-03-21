package com.example.rafiulislamrafi.locationalarm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class Add_Task extends Activity implements View.OnClickListener {

    DataBase_Helper myDatabase;

    int PLACE_PICKER_REQUEST = 1;

    public int mYear, mMonth, mDay, mHour, mMinute;

    public static String name, id, task, location;

    public static String Date, Time;

    EditText get_location, get_task, get_id, get_date, get_time;
    Button submit_button, view_button, update_buton, delete_button;

    ImageView map, clock, calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__task);

        //Hide all the titlebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDatabase = new DataBase_Helper(this);

        get_location = (EditText) findViewById(R.id.getLocation);
        get_task = (EditText) findViewById(R.id.getTask);
        get_id = (EditText) findViewById(R.id.id);
        get_date = (EditText) findViewById(R.id.getDate);
        get_time = (EditText) findViewById(R.id.getTime);

        map = (ImageView) findViewById(R.id.mapViewButton);
        clock = (ImageView) findViewById(R.id.clock);
        calender = (ImageView) findViewById(R.id.calender);

        submit_button = (Button) findViewById(R.id.submit);
        view_button = (Button) findViewById(R.id.view);
        update_buton = (Button) findViewById(R.id.update);
        delete_button = (Button) findViewById(R.id.delete);

        viewAllData();
        updateData();
        deleteData();

        clock.setOnClickListener(this);
        calender.setOnClickListener(this);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;
                try {
                    intent = builder.build(getApplicationContext());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int request_code, int result_code, Intent data) {

        if (request_code == PLACE_PICKER_REQUEST) {

            if (result_code == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);

                name = String.format("%s", place.getName());
                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;


                location = ""+latitude+","+longitude;


                get_location.setText("" + name);
            }
        }

        SubmitData(location);
    }


    public void onClick(View v) {

        if (v == calender) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            get_date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Toast.makeText(getApplicationContext(), "DATE : " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == clock) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            get_time.setText("" + hourOfDay + " : " + minute);

                            Toast.makeText(getApplicationContext(), "TIME - " + hourOfDay + " : " + minute, Toast.LENGTH_SHORT).show();

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


    public void SubmitData(final String user_location) {

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = get_id.getText().toString();

                task = get_task.getText().toString();

                boolean isInserted = myDatabase.insertData(id.toString(), user_location.toString(), task.toString(), get_time.getText().toString(), get_date.getText().toString());

                if (isInserted == true) {

                    Toast.makeText(Add_Task.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(Add_Task.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                }

                clearText();
            }
        });
    }

    public void updateData() {

        update_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isUpdate = myDatabase.updateData(get_id.getText().toString(), get_location.getText().toString(),
                        get_task.getText().toString(),
                        get_time.getText().toString(),
                        get_date.getText().toString());


                if (get_id.getText().toString() != null) {

                    if (isUpdate == true) {

                        Toast.makeText(getApplication(), "Data Updated", Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(getApplication(), "Data Not Updated", Toast.LENGTH_LONG).show();

                }

                clearText();

            }
        });
    }

    public void deleteData() {

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer deleteRow = myDatabase.deleteData(get_id.getText().toString());

                if (deleteRow > 0) {

                    Toast.makeText(getApplicationContext(), "Data Deleted", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Data Not Deleted", Toast.LENGTH_SHORT).show();
                }
                clearText();

            }
        });

    }

    public void clearText() {

        get_location.setText("");
        get_task.setText("");
        get_id.setText("");
        get_time.setText("");
        get_date.setText("");
    }

    public void viewAllData() {

        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor data = myDatabase.getAllData();

                if (data.getCount() == 0) {

                    //Show Message
                    showMessage("Error", "Nothing Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();

                while (data.moveToNext()) {

                    buffer.append("ID : " + data.getString(0) + " \n ");
                    buffer.append("\tLOCATION : " + data.getString(1) + " \n ");
                    //buffer.append("\tLOCATION : " + name + " \n ");
                    buffer.append("\tTASK : " + data.getString(2) + " \n ");
                    buffer.append("\tTIME : " + data.getString(3) + " \n");
                    buffer.append("\tDATE : " + data.getString(4) + " \n\n");
                }

                //Show all data
                showMessage("Data", buffer.toString());
            }
        });

    }

    public void showMessage(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

