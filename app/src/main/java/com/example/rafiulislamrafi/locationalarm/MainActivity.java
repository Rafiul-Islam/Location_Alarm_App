package com.example.rafiulislamrafi.locationalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button add_task_button, start_task_button, end_task_button, task_button, find_button;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide all the titlebar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        objCasting();
        onButtonClick();

    }

    public void objCasting() {

        add_task_button = (Button) findViewById(R.id.addTaskButton);
        start_task_button = (Button) findViewById(R.id.StartTaskButton);
        end_task_button = (Button) findViewById(R.id.EndTaskButton);
        task_button = (Button) findViewById(R.id.taskButton);
        find_button = (Button) findViewById(R.id.PlaceFinderButton);

    }

    public void onButtonClick() {

        add_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Task.class);
                startActivity(intent);
            }
        });

        task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Tasks.class);
                startActivity(intent);

            }
        });

        start_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, StartTask.class);
                startActivity(intent);

            }
        });

        end_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EndTask.class);
                startActivity(intent);

            }
        });

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Find_Place.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {

        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Alert Message");
        builder.setMessage("Do You Want To Close ? ");
        //builder.setIcon(R.drawable.question);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onDestroy();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "You deny to close", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "You pressed on cancel Button", Toast.LENGTH_SHORT).show();
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
