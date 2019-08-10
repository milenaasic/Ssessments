package com.ssessments.login_and_registration

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModel

class RegistrationSharedViewModelFactory (
    val database: NewsDatabaseDao,
    val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationSharedViewModel::class.java)) {
            return RegistrationSharedViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}