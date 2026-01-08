package com.mapapp.repo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.mapapp.model.Place
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.*

class PlacesRepository(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { continuation ->
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                continuation.resumeWithException(SecurityException("Location permission not granted"))
                return@suspendCancellableCoroutine
            }

            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (lastKnownLocation != null) {
                continuation.resume(lastKnownLocation)
            } else {
                // Create a mock location for demo purposes
                val mockLocation = Location("mock").apply {
                    latitude = 28.6139
                    longitude = 77.2090
                }
                continuation.resume(mockLocation)
            }
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    suspend fun getNearbyPlaces(location: Location): List<Place> {
        // Mock data with calculated distances
        val mockPlaces = listOf(
            Place("Coffee Shop", 28.6145, 77.2095, 0.0),
            Place("Restaurant", 28.6135, 77.2085, 0.0),
            Place("Gas Station", 28.6150, 77.2100, 0.0),
            Place("Pharmacy", 28.6130, 77.2080, 0.0),
            Place("Bank", 25.2155, 55.2105, 0.0)
        )

        return mockPlaces.map { place ->
            val distance = calculateDistance(
                location.latitude, location.longitude,
                place.latitude, place.longitude
            )
            place.copy(distance = distance)
        }.sortedBy { it.distance }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}