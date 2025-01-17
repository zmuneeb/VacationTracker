package com.example.d308;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.d308.entities.Excursion;
import com.example.d308.viewmodel.ExcursionViewModel;
import java.util.ArrayList;
import java.util.List;

public class ExcursionListActivity extends AppCompatActivity {
    private ExcursionViewModel excursionViewModel;
    private ExcursionAdapter adapter;
    private BroadcastReceiver vacationAlarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            new AlertDialog.Builder(ExcursionListActivity.this)
                    .setTitle("Alarm")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    private void filter(String text) {
        if (text.isEmpty()) {
            adapter.filterList(new ArrayList<>(adapter.originalExcursions));
        } else {
            List<Excursion> filteredList = new ArrayList<>();
            for (Excursion excursion : adapter.originalExcursions) {
                if (excursion.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(excursion);
                }
            }
            adapter.filterList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(vacationAlarmReceiver, new IntentFilter("VacationAlarm"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(vacationAlarmReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excursionlist);

        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        ListView listView = findViewById(R.id.listView);
        TextView emptyView = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);

        adapter = new ExcursionAdapter(ExcursionListActivity.this, new ArrayList<>());
        listView.setAdapter(adapter);
        excursionViewModel.getAllExcursions().observe(this, new Observer<List<Excursion>>() {
            @Override
            public void onChanged(List<Excursion> excursions) {
                adapter.clear();
                adapter.addAll(excursions);
                adapter.originalExcursions = new ArrayList<>(excursions);
                adapter.notifyDataSetChanged();
            }
        });

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button addExcursionButton = findViewById(R.id.addExcursionButton);
        addExcursionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExcursionListActivity.this, ExcursionActivity.class);
                startActivity(intent);
            }
        });
        Button vacationListButton = findViewById(R.id.vacationListButton);
        vacationListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExcursionListActivity.this, VacationListActivity.class);
                startActivity(intent);
            }
        });
    }
    private class ExcursionAdapter extends ArrayAdapter<Excursion> {
        private Context context;

        public ExcursionAdapter(Context context, List<Excursion> excursions) {
            super(context, R.layout.excursion_item, excursions);
            this.context = context;
        }

        private List<Excursion> originalExcursions;

        public List<Excursion> getAllExcursions() {
            List<Excursion> allExcursions = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                allExcursions.add(getItem(i));
            }
            return allExcursions;
        }

        public void filterList(List<Excursion> filteredList) {
            clear();
            addAll(filteredList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.excursion_item, parent, false);
            }

            Excursion excursion = getItem(position);
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            nameTextView.setText(excursion.getName());
            ImageView deleteButton = convertView.findViewById(R.id.deleteButton);
            Button updateButton = convertView.findViewById(R.id.updateButton);

            deleteButton.setOnClickListener(v -> {
                excursionViewModel.delete(excursion);
            });

            updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ExcursionActivity.class);
                intent.putExtra("excursionId", excursion.getId());
                context.startActivity(intent);
            });
            return convertView;
        }
    }
}
