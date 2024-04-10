package com.example.d308;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.ExcursionViewModel;
import com.example.d308.viewmodel.VacationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageVacationActivity extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private ExcursionViewModel excursionViewModel;
    private Vacation currentVacation;
    private EditText nameEditText;
    private Button startDateButton;
    private Button endDateButton;
    private int userId;
    private List<Excursion> selectedExcursionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation);
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        nameEditText = findViewById(R.id.nameEditText);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        String username = getIntent().getStringExtra("username");

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
                    currentVacation = vacation;
                }
            });
        }

        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        excursionViewModel.getExcursionsForVacation(vacationId).observe(this, new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
            }
        });

        final Calendar startDateCalendar = Calendar.getInstance();
        final Calendar endDateCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, monthOfYear);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
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

        Button addExcursionButton = findViewById(R.id.addExcursionButton);
        addExcursionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExcursionSelectionDialog();
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
                    currentVacation.setUserId(userId);

                    if (currentVacation.getId() == 0) {
                        vacationViewModel.insert(currentVacation);
                        vacationViewModel.getLastVacation().observe(ManageVacationActivity.this, new Observer<Vacation>() {
                            @Override
                            public void onChanged(Vacation vacation) {
                                currentVacation = vacation;
                                for (Excursion excursion : selectedExcursionsList) {
                                    excursion.setVacationId(currentVacation.getId());
                                    excursionViewModel.update(excursion); // Update the excursion instead of inserting it
                                }
                                vacationViewModel.getLastVacation().removeObserver(this);
                            }
                        });
                    } else {
                        vacationViewModel.update(currentVacation);
                        for (Excursion excursion : selectedExcursionsList) {
                            excursion.setVacationId(currentVacation.getId());
                            excursionViewModel.update(excursion); // Update the excursion instead of inserting it
                        }
                    }
                }
                if (currentVacation != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                    try {
                        Date vacationStartDate = sdf.parse(currentVacation.getStartDate());
                        Date vacationEndDate = sdf.parse(currentVacation.getEndDate());

                        for (Excursion excursion : selectedExcursionsList) {
                            Date excursionDate = sdf.parse(excursion.getDate());
                            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                                Toast.makeText(ManageVacationActivity.this, "The excursion " + excursion.getName() + " is not within the vacation time.", Toast.LENGTH_SHORT).show();
                            } else {
                                excursion.setVacationId(currentVacation.getId());
                                excursionViewModel.update(excursion);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (currentVacation != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                    try {
                        Date vacationStartDate = sdf.parse(currentVacation.getStartDate());
                        Date vacationEndDate = sdf.parse(currentVacation.getEndDate());

                        // Set up the start date alarm
                        Intent startIntent = new Intent(ManageVacationActivity.this, VacationAlarmReceiver.class);
                        startIntent.putExtra(VacationAlarmReceiver.EXTRA_TITLE, currentVacation.getName());
                        startIntent.putExtra(VacationAlarmReceiver.EXTRA_TYPE, "Start");
                        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
                        PendingIntent startPendingIntent = PendingIntent.getBroadcast(ManageVacationActivity.this, 0, startIntent, flags);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, vacationStartDate.getTime(), startPendingIntent);

                        // Set up the end date alarm
                        Intent endIntent = new Intent(ManageVacationActivity.this, VacationAlarmReceiver.class);
                        endIntent.putExtra(VacationAlarmReceiver.EXTRA_TITLE, currentVacation.getName());
                        endIntent.putExtra(VacationAlarmReceiver.EXTRA_TYPE, "End");
                        PendingIntent endPendingIntent = PendingIntent.getBroadcast(ManageVacationActivity.this, 1, endIntent, flags);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, vacationEndDate.getTime(), endPendingIntent);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
    }
    private void showExcursionSelectionDialog() {
        MutableLiveData<List<Excursion>> mutableLiveData = new MutableLiveData<>();

        mutableLiveData.observe(this, new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> allExcursions) {
                if (allExcursions == null || allExcursions.isEmpty()) {
                    Toast.makeText(ManageVacationActivity.this, "No excursions available", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] excursionNames = new String[allExcursions.size()];
                for (int i = 0; i < allExcursions.size(); i++) {
                    excursionNames[i] = allExcursions.get(i).getName();
                }

                final boolean[] selectedExcursions = new boolean[allExcursions.size()];

                new AlertDialog.Builder(ManageVacationActivity.this)
                        .setTitle("Select Excursions")
                        .setMultiChoiceItems(excursionNames, selectedExcursions, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selectedExcursions[which] = isChecked;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExcursionsList.clear();

                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                                try {
                                    Date vacationStartDate = sdf.parse(startDateButton.getText().toString());
                                    Date vacationEndDate = sdf.parse(endDateButton.getText().toString());

                                    for (int i = 0; i < allExcursions.size(); i++) {
                                        if (selectedExcursions[i]) {
                                            Excursion selectedExcursion = allExcursions.get(i);
                                            Date excursionDate = sdf.parse(selectedExcursion.getDate());

                                            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                                                Toast.makeText(ManageVacationActivity.this, "The excursion " + selectedExcursion.getName() + " is not within the vacation time.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                selectedExcursionsList.add(selectedExcursion);
                                            }
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // Remove the observer
                                mutableLiveData.removeObservers(ManageVacationActivity.this);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        excursionViewModel.getAllExcursions().observe(this, new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> allExcursions) {
                mutableLiveData.setValue(allExcursions);
                excursionViewModel.getAllExcursions().removeObserver(this);
            }
        });
    }
}