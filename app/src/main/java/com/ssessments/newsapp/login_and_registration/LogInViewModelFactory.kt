package com.ssessments.newsapp.login_and_registration

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao

class LogInViewModelFactory (
    val database: NewsDatabaseDao,
    val application: Application
): ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogIn_ViewModel::class.java)) {
            return LogIn_ViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}