package com.example.rafiulislamrafi.locationalarm;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Find_Place extends Activity {

    TextView textView, textView2, textView3, textView4, textView5;
    ImageView button;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__place);

        //Hide all the titlebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView = (TextView) findViewById(R.id.name);
        textView2 = (TextView) findViewById(R.id.address);
        textView3 = (TextView) findViewById(R.id.locale);
        textView4 = (TextView) findViewById(R.id.type);
        textView5 = (TextView) findViewById(R.id.rating);

        button = (ImageView) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
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

                String name = String.format("Place Name :  %s", place.getName());
                String address = String.format("Place Address :  %s", place.getAddress());
                String local_name = String.format("Phone Number :  %s", place.getPhoneNumber());
                String type = String.format("Website Uri :  %s", place.getWebsiteUri());
                String rating = String.format("Place Rating :  %s", place.getRating() + " / 5");

                textView.setText(name);
                textView2.setText(address);
                textView3.setText(local_name);
                textView4.setText(type);

                if (place.getRating() > 0){
                    textView5.setText(rating);
                }
                else{
                    rating = "Place Rating : 0";
                    textView5.setText(rating);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        finish();

        super.onDestroy();
    }
}
