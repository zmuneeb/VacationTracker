package com.example.d308;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.ExcursionViewModel;
import com.example.d308.viewmodel.VacationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewVacationActivity extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private ExcursionViewModel excursionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vacation);

        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        int vacationId = getIntent().getIntExtra("vacationId", -1);
        if (vacationId != -1) {
            vacationViewModel.getVacationById(vacationId).observe(this, new Observer<Vacation>() {
                @Override
                public void onChanged(Vacation vacation) {
                    List<String> vacationDetails = new ArrayList<>();
                    vacationDetails.add("Name: " + vacation.getName());
                    vacationDetails.add("Location: " + vacation.getLocation());
                    vacationDetails.add("Place of Stay: " + vacation.getPlaceOfStay());
                    vacationDetails.add("Start Date: " + vacation.getStartDate());
                    vacationDetails.add("End Date: " + vacation.getEndDate());

                    ListView vacationDetailsListView = findViewById(R.id.vacationDetailsListView);
                    ArrayAdapter<String> vacationDetailsAdapter = new ArrayAdapter<>(ViewVacationActivity.this, android.R.layout.simple_list_item_1, vacationDetails);
                    vacationDetailsListView.setAdapter(vacationDetailsAdapter);
                }
            });

            excursionViewModel.getExcursionsForVacation(vacationId).observe(this, new Observer<List<Excursion>>() {
                @Override
                public void onChanged(List<Excursion> excursions) {
                    List<String> excursionNames = new ArrayList<>();
                    for (Excursion excursion : excursions) {
                        String detail = excursion.getName() + " on " + excursion.getDate();
                        excursionNames.add(detail);
                    }

                    ListView excursionListView = findViewById(R.id.excursionListView);
                    ArrayAdapter<String> excursionAdapter = new ArrayAdapter<>(ViewVacationActivity.this, android.R.layout.simple_list_item_1, excursionNames);
                    excursionListView.setAdapter(excursionAdapter);
                }
            });
        }
    }
}