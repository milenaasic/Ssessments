package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssessments.newsapp.database.NewsDatabaseDao

class SavedFiltersViewModelFactory (
    val database: NewsDatabaseDao,
    val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedFiltersViewModel::class.java)) {
            return SavedFiltersViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
