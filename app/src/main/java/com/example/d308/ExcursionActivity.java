package com.example.d308;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308.entities.Excursion;
import com.example.d308.viewmodel.ExcursionViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExcursionActivity extends AppCompatActivity {
    private ExcursionViewModel excursionViewModel;
    private Excursion currentExcursion;
    private EditText nameEditText;
    private Button dateButton;
    private Calendar dateCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excursion);

        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        nameEditText = findViewById(R.id.nameEditText);
        dateButton = findViewById(R.id.dateButton);

        int excursionId = getIntent().getIntExtra("excursionId", -1);
        if (excursionId != -1) {
            excursionViewModel.getExcursionById(excursionId).observe(this, new Observer<Excursion>() {
                @Override
                public void onChanged(Excursion excursion) {
                    nameEditText.setText(excursion.getName());
                    dateButton.setText(excursion.getDate());
                    currentExcursion = excursion;
                }
            });
        }

        nameEditText = findViewById(R.id.nameEditText);
        dateButton = findViewById(R.id.dateButton);
        final Calendar dateCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateButton.setText(sdf.format(dateCalendar.getTime()));
            }
        };

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExcursionActivity.this, datePicker, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String date = dateButton.getText().toString();

                if (name.isEmpty() || date.isEmpty()) {
                    Toast.makeText(ExcursionActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (currentExcursion == null) {
                        currentExcursion = new Excursion();
                        currentExcursion.setName(name);
                        currentExcursion.setDate(date);
                        excursionViewModel.insert(currentExcursion);
                    } else {
                        currentExcursion.setName(name);
                        currentExcursion.setDate(date);
                        excursionViewModel.update(currentExcursion);
                    }

                    finish();
                }
            }
        });
    }

    private void updateDateButtonText() {
        dateButton.setText(dateFormat.format(dateCalendar.getTime()));
    }
}