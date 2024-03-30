package com.example.d308;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.example.d308.viewmodel.ExcursionViewModel;
import com.example.d308.viewmodel.VacationViewModel;
import java.util.ArrayList;
import java.util.List;

public class VacationListActivity extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private ExcursionViewModel excursionViewModel;
    private VacationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacationlist);

        // Initialize the ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        // Set up the ListView and the empty view
        ListView listView = findViewById(R.id.listView);
        TextView emptyView = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);

        adapter = new VacationAdapter(VacationListActivity.this, new ArrayList<>());
        listView.setAdapter(adapter);
        vacationViewModel.getAllVacations().observe(this, new Observer<List<Vacation>>() {
            @Override
            public void onChanged(List<Vacation> vacations) {
                // Update the ListView
                adapter.clear();
                adapter.addAll(vacations);
                adapter.notifyDataSetChanged();
            }
        });

        Button addVacationButton = findViewById(R.id.addVacationButton);
        addVacationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationListActivity.this, ManageVacationActivity.class);
                startActivity(intent);
            }
        });
        Button excursionListButton = findViewById(R.id.excursionListButton);
        excursionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationListActivity.this, ExcursionListActivity.class);
                startActivity(intent);
            }
        });
    }
    private class VacationAdapter extends ArrayAdapter<Vacation> {
        private Context context;

        public VacationAdapter(Context context, List<Vacation> vacations) {
            super(context, R.layout.vacation_item, vacations);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.vacation_item, parent, false);
            }

            Vacation vacation = getItem(position);

            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            Button viewButton = convertView.findViewById(R.id.viewButton);
            Button deleteButton = convertView.findViewById(R.id.deleteButton);
            Button updateButton = convertView.findViewById(R.id.updateButton);

            nameTextView.setText(vacation.getName());

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewVacationActivity.class);
                    intent.putExtra("vacationId", vacation.getId());
                    context.startActivity(intent);
                }
            });

            updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ManageVacationActivity.class);
                intent.putExtra("vacationId", vacation.getId());
                context.startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> {
                excursionViewModel.getExcursionsForVacation(vacation.getId()).observe(VacationListActivity.this, new Observer<List<Excursion>>() {
                    @Override
                    public void onChanged(List<Excursion> excursions) {
                        if (excursions != null && !excursions.isEmpty()) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Warning")
                                    .setMessage("This vacation has excursions associated with it. Are you sure you want to delete it?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            vacationViewModel.delete(vacation);
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        } else {
                            vacationViewModel.delete(vacation);
                        }
                        excursionViewModel.getExcursionsForVacation(vacation.getId()).removeObserver(this);
                    }
                });
            });
            return convertView;
        }
    }
}