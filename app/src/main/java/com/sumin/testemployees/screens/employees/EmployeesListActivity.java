package com.sumin.testemployees.screens.employees;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.sumin.testemployees.R;
import com.sumin.testemployees.adapters.EmployeeAdapter;
import com.sumin.testemployees.pojo.Employee;
import com.sumin.testemployees.pojo.Speciality;

import java.util.ArrayList;
import java.util.List;

public class EmployeesListActivity extends AppCompatActivity{

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter adapter;
    private EmployeesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_list);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        adapter = new EmployeeAdapter();
        adapter.setEmployees(new ArrayList<Employee>());
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmployees.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(EmployeesViewModel.class);
        viewModel.loadData();
        viewModel.getAllEmployees().observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(@Nullable List<Employee> employees) {
                adapter.setEmployees(employees);
                if (employees == null) return;
                for (Employee employee: employees) {
                    for (Speciality speciality: employee.getSpecialty()) {
                        Log.i("ConverterTest", speciality.getName());
                    }
                }
            }
        });
        viewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                if (throwable != null) {
                    Toast.makeText(EmployeesListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    viewModel.getError().setValue(null);
                }
            }
        });
    }
}
