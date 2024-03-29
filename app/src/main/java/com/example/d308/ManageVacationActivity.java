package com.example.d308;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.VacationViewModel;

public class ManageVacationActivity extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private Vacation currentVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation);
        // Initialize the ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Initialize currentVacation
        currentVacation = new Vacation();

        // Set up the Add Excursion CheckBox and the Excursion Name EditText
        CheckBox addExcursionCheckBox = findViewById(R.id.addExcursionCheckBox);
        EditText excursionNameEditText = findViewById(R.id.excursionNameEditText);

        addExcursionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the CheckBox is checked, show the EditText
                    excursionNameEditText.setVisibility(View.VISIBLE);
                } else {
                    // If the CheckBox is not checked, hide the EditText
                    excursionNameEditText.setVisibility(View.GONE);
                }
            }
        });

        // Set up the Save button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered details
                String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
                String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
                String placeOfStay = ((EditText) findViewById(R.id.placeOfStayEditText)).getText().toString();
                String startDate = ((EditText) findViewById(R.id.startDateEditText)).getText().toString();
                String endDate = ((EditText) findViewById(R.id.endDateEditText)).getText().toString();

                // Create or update the Vacation object
                if (currentVacation == null) {
                    currentVacation = new Vacation();
                }
                currentVacation.setName(name);
                currentVacation.setLocation(location);
                currentVacation.setPlaceOfStay(placeOfStay);
                currentVacation.setStartDate(startDate);
                currentVacation.setEndDate(endDate);

                // Get the entered excursion name if the CheckBox is checked
                if (addExcursionCheckBox.isChecked()) {
                    String excursionName = excursionNameEditText.getText().toString();
                    // Add the excursion to the Vacation object
                    currentVacation.setExcursionName(excursionName);
                }

                // Save or update the vacation
                if (currentVacation.getId() == 0) {
                    vacationViewModel.insert(currentVacation);
                } else {
                    vacationViewModel.update(currentVacation);
                }

                // Close the activity
                finish();
            }
        });
    }
}
