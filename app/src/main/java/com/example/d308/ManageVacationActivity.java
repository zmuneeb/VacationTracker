package com.example.d308;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.VacationViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ManageVacationActivity extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private Vacation currentVacation;
    private EditText nameEditText;
    private Button startDateButton;
    private Button endDateButton;
    private EditText excursionNameEditText;
    private Button excursionDateButton;
    private CheckBox addExcursionCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation);
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        nameEditText = findViewById(R.id.nameEditText);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        excursionNameEditText = findViewById(R.id.excursionNameEditText);
        excursionDateButton = findViewById(R.id.excursionDate);
        addExcursionCheckBox = findViewById(R.id.addExcursionCheckBox);

        int vacationId = getIntent().getIntExtra("vacationId", -1);
        if (vacationId != -1) {
            vacationViewModel.getVacationById(vacationId).observe(this, new Observer<Vacation>() {
                @Override
                public void onChanged(Vacation vacation) {
                    nameEditText.setText(vacation.getName());
                    // Assuming you have the corresponding getters in your Vacation class
                    ((EditText) findViewById(R.id.locationEditText)).setText(vacation.getLocation());
                    ((EditText) findViewById(R.id.placeOfStayEditText)).setText(vacation.getPlaceOfStay());
                    startDateButton.setText(vacation.getStartDate());
                    endDateButton.setText(vacation.getEndDate());
                    excursionNameEditText.setText(vacation.getExcursionName());
                    currentVacation = vacation;
                }
            });
        }

        final Calendar startDateCalendar = Calendar.getInstance();
        final Calendar endDateCalendar = Calendar.getInstance();
        final Calendar excursionDateCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, monthOfYear);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDateButton.setText(sdf.format(startDateCalendar.getTime()));
            }
        };

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManageVacationActivity.this, startDatePicker, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (selectedDate.before(startDateCalendar)) {
                    Toast.makeText(ManageVacationActivity.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                } else {
                    endDateCalendar.set(Calendar.YEAR, year);
                    endDateCalendar.set(Calendar.MONTH, monthOfYear);
                    endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "MM/dd/yy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    endDateButton.setText(sdf.format(endDateCalendar.getTime()));
                }
            }
        };

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManageVacationActivity.this, endDatePicker, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener excursionDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (selectedDate.before(startDateCalendar) || selectedDate.after(endDateCalendar)) {
                    Toast.makeText(ManageVacationActivity.this, "Excursion date must be within the vacation dates", Toast.LENGTH_SHORT).show();
                } else {
                    excursionDateCalendar.set(Calendar.YEAR, year);
                    excursionDateCalendar.set(Calendar.MONTH, monthOfYear);
                    excursionDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "MM/dd/yy"; // your format
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    excursionDateButton.setText(sdf.format(excursionDateCalendar.getTime()));
                }
            }
        };

        excursionDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManageVacationActivity.this, excursionDatePicker, excursionDateCalendar
                        .get(Calendar.YEAR), excursionDateCalendar.get(Calendar.MONTH),
                        excursionDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addExcursionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    excursionNameEditText.setVisibility(View.VISIBLE);
                    excursionDateButton.setVisibility(View.VISIBLE);
                } else {
                    excursionNameEditText.setVisibility(View.GONE);
                    excursionDateButton.setVisibility(View.GONE);
                }
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
                String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
                String placeOfStay = ((EditText) findViewById(R.id.placeOfStayEditText)).getText().toString();
                String startDate = startDateButton.getText().toString();
                String endDate = endDateButton.getText().toString();

                if (name.isEmpty() || location.isEmpty() || placeOfStay.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    Toast.makeText(ManageVacationActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (currentVacation == null) {
                        currentVacation = new Vacation();
                    }
                    currentVacation.setName(name);
                    currentVacation.setLocation(location);
                    currentVacation.setPlaceOfStay(placeOfStay);
                    currentVacation.setStartDate(startDate);
                    currentVacation.setEndDate(endDate);

                    String excursionName = excursionNameEditText.getText().toString();
                    if (!excursionName.isEmpty()) {
                        currentVacation.setExcursionName(excursionName);
                    }

                    if (currentVacation.getId() == 0) {
                        vacationViewModel.insert(currentVacation);
                    } else {
                        vacationViewModel.update(currentVacation);
                    }
                    finish();
                }
            }
        });
    }
}