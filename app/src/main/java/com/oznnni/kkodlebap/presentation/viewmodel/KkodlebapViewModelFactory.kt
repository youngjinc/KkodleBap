package com.oznnni.kkodlebap.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KkodlebapViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaygroundViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaygroundViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown PlaygroundViewModel class")
    }
}
