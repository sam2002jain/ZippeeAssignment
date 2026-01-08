package com.mapapp.ui

import com.facebook.react.bridge.*
import com.mapapp.repo.PlacesRepository
import kotlinx.coroutines.*

class NearbyPlacesModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    private val repository: PlacesRepository

    init {
        repository = PlacesRepository(reactContext)
    }

    override fun getName() = "NearbyPlacesModule"

    @ReactMethod
    fun getNearbyPlaces(promise: Promise) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val location = repository.getCurrentLocation()
                
                val places = repository.getNearbyPlaces(location)
                
                withContext(Dispatchers.Main) {
                    val array = Arguments.createArray()
                    places.forEach { place ->
                        val map = Arguments.createMap()
                        map.putString("name", place.name)
                        map.putDouble("latitude", place.latitude)
                        map.putDouble("longitude", place.longitude)
                        map.putDouble("distance", place.distance)
                        array.pushMap(map)
                    }

                    val result = Arguments.createMap()
                    result.putArray("places", array)
                    promise.resolve(result)
                }

            } catch (e: SecurityException) {
                withContext(Dispatchers.Main) {
                    promise.reject("PERMISSION_ERROR", "Location permission required")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    promise.reject("ERROR", e.message ?: "Unknown error occurred")
                }
            }
        }
    }

    @ReactMethod
    fun requestLocationPermission(promise: Promise) {
        promise.resolve(true)
    }
}
