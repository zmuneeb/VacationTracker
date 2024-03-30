package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the View Vacations button
        Button vacationListButton = findViewById(R.id.vacationListButton);
        vacationListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to VacationListActivity
                Intent intent = new Intent(MainActivity.this, VacationListActivity.class);
                startActivity(intent);
            }
        });
        Button excursionListButton = findViewById(R.id.excursionListButton);
        excursionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ExcursionListActivity
                Intent intent = new Intent(MainActivity.this, ExcursionListActivity.class);
                startActivity(intent);
            }
        });
    }
}