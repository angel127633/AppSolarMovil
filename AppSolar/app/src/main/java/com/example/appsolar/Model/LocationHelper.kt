package com.example.appsolar.Model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

class LocationHelper(
    private val context: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onLocation: (Location?) -> Unit
    ) {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onLocation(location)
            }
    }
}