package com.example.d308;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.d308.entities.Excursion;
import com.example.d308.viewmodel.ExcursionViewModel;
import java.util.ArrayList;
import java.util.List;

public class ExcursionListActivity extends AppCompatActivity {
    private ExcursionViewModel excursionViewModel;
    private ExcursionAdapter adapter;

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
                adapter.notifyDataSetChanged();
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

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.excursion_item, parent, false);
            }

            Excursion excursion = getItem(position);
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            nameTextView.setText(excursion.getName());
            Button deleteButton = convertView.findViewById(R.id.deleteButton);
            Button updateButton = convertView.findViewById(R.id.updateButton);

            deleteButton.setOnClickListener(v -> {
                // Delete the excursion
                excursionViewModel.delete(excursion);
            });

            updateButton.setOnClickListener(v -> {
                // Update the excursion
                Intent intent = new Intent(context, ExcursionActivity.class);
                intent.putExtra("excursionId", excursion.getId());
                context.startActivity(intent);
            });
            return convertView;
        }
    }
}
