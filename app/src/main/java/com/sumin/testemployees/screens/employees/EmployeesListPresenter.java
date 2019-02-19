package com.sumin.testemployees.screens.employees;

import android.widget.Toast;

import com.sumin.testemployees.api.ApiFactory;
import com.sumin.testemployees.api.ApiService;
import com.sumin.testemployees.pojo.EmployeeResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmployeesListPresenter {

    private CompositeDisposable compositeDisposable;
    private EmployeeListView view;

    public EmployeesListPresenter(EmployeeListView view) {
        compositeDisposable = new CompositeDisposable();
        this.view = view;
    }

    public void loadData() {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EmployeeResponse>() {
                    @Override
                    public void accept(EmployeeResponse employeeResponse) throws Exception {
                        view.showEmployees(employeeResponse.getResponse());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void disposeDisposable() {
        compositeDisposable.dispose();
    }
}
