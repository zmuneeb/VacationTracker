package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button vacationListButton = findViewById(R.id.vacationListButton);
        vacationListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VacationListActivity.class);
                intent.putExtra("username", MainActivity.currentUsername);
                startActivity(intent);
            }
        });
        Button excursionListButton = findViewById(R.id.excursionListButton);
        excursionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExcursionListActivity.class);
                intent.putExtra("username", MainActivity.currentUsername);
                startActivity(intent);
            }
        });
    }
}