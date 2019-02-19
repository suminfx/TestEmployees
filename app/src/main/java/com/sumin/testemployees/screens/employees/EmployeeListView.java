package com.sumin.testemployees.screens.employees;

import com.sumin.testemployees.pojo.Employee;

import java.util.List;

public interface EmployeeListView {
    void showEmployees(List<Employee> employees);
    void showError(Throwable throwable);
}
