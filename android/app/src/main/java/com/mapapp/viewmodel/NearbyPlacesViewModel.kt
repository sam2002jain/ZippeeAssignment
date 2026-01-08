package com.mapapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapapp.model.Place
import com.mapapp.repo.PlacesRepository
import kotlinx.coroutines.launch

class NearbyPlacesViewModel(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadPlaces() {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                _error.postValue(null)
                
                val location = repository.getCurrentLocation()
                val placesList = repository.getNearbyPlaces(location)
                _places.postValue(placesList)
            } catch (e: SecurityException) {
                _error.postValue("Location permission required")
            } catch (e: Exception) {
                _error.postValue("Failed to load places: ${e.message}")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun clearError() {
        _error.postValue(null)
    }
}
