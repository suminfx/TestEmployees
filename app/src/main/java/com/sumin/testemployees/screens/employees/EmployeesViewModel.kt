package com.sumin.testemployees.screens.employees

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask

import com.sumin.testemployees.api.ApiFactory
import com.sumin.testemployees.data.AppDatabase
import com.sumin.testemployees.pojo.Employee

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EmployeesViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable: CompositeDisposable
    val allEmployees: LiveData<List<Employee>>
    val error: MutableLiveData<Throwable> = MutableLiveData()

    init {
        appDatabase = AppDatabase.getInstance(application)
        compositeDisposable = CompositeDisposable()
        allEmployees = appDatabase.employeeDao().allEmployees
    }


    private fun insertEmployees(employees: List<Employee>) {
        InsertEmployeesTask().execute(employees)
    }

    private fun deleteAllEmployees() {
        DeleteAllEmployeesTask().execute()
    }

    private class DeleteAllEmployeesTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            appDatabase.employeeDao().deleteAllEmployees()
            return null
        }
    }

    private class InsertEmployeesTask : AsyncTask<List<Employee>, Void, Void>() {
        @SafeVarargs
        override fun doInBackground(vararg lists: List<Employee>): Void? {
            if (lists.isNotEmpty()) {
                appDatabase.employeeDao().insertEmployees(lists[0])
            }
            return null
        }
    }

    fun loadData() {
        val apiFactory = ApiFactory.getInstance()
        val apiService = apiFactory.apiService
        val disposable = apiService.employees
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ employeeResponse ->
                    deleteAllEmployees()
                    insertEmployees(employeeResponse.response)
                }, { throwable ->
                    error.value = throwable
                })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    companion object {
        lateinit var appDatabase: AppDatabase
    }
}
