package com.example.countries.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.countries.api.countries.CountriesApi
import com.example.countries.api.countries.Country
import kotlinx.coroutines.launch

class MainViewModel(
    private val countriesApi: CountriesApi
) : ViewModel() {
    private val _countries = MutableLiveData<List<Country>>(emptyList())
    val countries = liveData { emitSource(_countries) }

    private val _error = MutableLiveData<String>()
    val error = liveData { emitSource(_error) }

    fun updateCountries() = viewModelScope.launch {
        try {
            _countries.value = countriesApi.getCountries()
        } catch (ex: Exception) {
            Log.e("MainViewModel", "Failed to update country list", ex)
            _countries.value = emptyList()
            _error.value = "Update failed"
        }
    }
}

class MainViewModelFactory(
    private val countriesApi: CountriesApi
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java -> MainViewModel(countriesApi) as T
            else -> super.create(modelClass)
        }
}
