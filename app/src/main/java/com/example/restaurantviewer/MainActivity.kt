package com.example.restaurantviewer

import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.restaurantviewer.data.RestaurantViewModel
import com.example.restaurantviewer.fragments.FavoriteFragment
import com.example.restaurantviewer.fragments.HomeFragment
import com.google.android.gms.location.FusedLocationProviderClient
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: RestaurantViewModel
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val permissionId = 2

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navHostFragment.navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

      bottomNavigationBar.setupWithNavController(navController)

        var cityLocation = "NewYork"



        Log.d("API_data_home", "Loaded data: ${viewModel.restaurantList.value}")


        getLastLocation { city ->
            cityLocation = city
        }
        Log.d("CityLocation", "City location: $cityLocation")


        viewModel.fetchData(cityLocation)
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(callback: (String) -> Unit){
        if(checkPermissions()){
            if(isLocationEnabled()){
                mFusedLocationClient.lastLocation.addOnCompleteListener{task ->
                    var location: Location? = task.result
                    if (location ==null){
                        getNewLocation()
                    }else{
                        Log.d("LocationServiceMessage","current location ${location.latitude} ${location.longitude}")
                        Log.d("LocationServiceMessage",
                            getCityNameWithRetry(location.latitude,location.longitude))

                            val city = getCityNameWithRetry(location.latitude,location.longitude)
                            callback(city)
                    }
                }
            }else{
                Toast.makeText(this,"Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermissions()
        }
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            var lastLocation: Location? = p0.lastLocation
            if (lastLocation != null) {
                Log.d("LocationCallbackMessage","current location ${lastLocation.longitude} ${lastLocation.latitude}")
            }
        }
    }

        @SuppressLint("MissingPermission")
        private fun getNewLocation(){
            locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 0
            locationRequest.fastestInterval = 0
            locationRequest.numUpdates = 2
             mFusedLocationClient!!.requestLocationUpdates(
                 locationRequest, locationCallback,Looper.myLooper()
             )
        }


    private fun getCityNameWithRetry(lat: Double, long: Double): String {
        val maxRetries = 3
        var attempt = 0
        var cityName = "Unknown"
        val geoCoder = Geocoder(this, Locale.getDefault())

        while (attempt < maxRetries) {
            try {
                val addresses = geoCoder.getFromLocation(lat, long, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    cityName = addresses[0].locality ?: "Unknown"
                    break
                }
            } catch (e: IOException) {
                Log.e("Geocoder", "Error getting city name: ${e.message}")
            }
            attempt++
            Thread.sleep(1000)
        }

        Log.d("CityName", "Current location: $cityName")
        return cityName
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("OnLocationPermission","granted permissionssion")
        }
    }
    }

