//code Attribution
//Android Developers. (n.d.). Android Developer Documentation. Available at: https://developer.android.com/docs.
//
//Android Jetpack. (n.d.). Android Jetpack. Available at: https://developer.android.com/jetpack.
//
//Kotlinlang.org. (n.d.). Kotlin Documentation. Available at: https://kotlinlang.org/docs/home.html.
//
//Kotlinlang.org. (n.d.). Kotlin by Example. Available at: https://kotlinlang.org/docs/examples.html.
//
//Firebase. (n.d.). Firebase Documentation. Available at: https://firebase.google.com/docs.
//
//Firebase. (n.d.). Firebase Samples on GitHub. Available at: https://github.com/firebase/quickstart-android.
//
//Google Maps Platform. (n.d.). Google Maps Platform Documentation. Available at: https://developers.google.com/maps/documentation/android-sdk/start.
//
//Google. (n.d.). Material Design Guidelines. Available at: https://material.io/design.
//
//Stack Overflow. (n.d.). Stack Overflow. Available at: https://stackoverflow.com.
//
//Udacity. (n.d.). Android Developer Nanodegree. Available at: https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801.
//
//Coursera. (n.d.). Android App Development Specialization. Available at: https://www.coursera.org/specializations/android-app-development.
//
//GitHub. (n.d.). GitHub Repositories. Available at: https://github.com.
//
//Google Cloud. (n.d.). Google Cloud Translation. Available at: https://cloud.google.com/translate/docs.
//
//Android Weekly. (n.d.). Android Weekly. Available at: https://androidweekly.net.
//
//Reddit. (n.d.). r/androiddev. Available at: https://www.reddit.com/r/androiddev/.

package com.example.laundryapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class TrackDriverActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var driverLocation: TextView
    private lateinit var eta: TextView
    private lateinit var database: DatabaseReference

    private val driverPhoneNumber = "0636806966"
    private val driverId = "driver"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_driver)

        driverLocation = findViewById(R.id.driver_location)
        eta = findViewById(R.id.eta)

        // Initialize the map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Call driver button
        findViewById<Button>(R.id.button_call_driver).setOnClickListener {
            callDriver()
        }

        // Message driver button
        findViewById<Button>(R.id.button_message_driver).setOnClickListener {
            messageDriver()
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("drivers").child(driverId)

        // Listen for location updates
        listenForDriverLocation()
    }

    private fun listenForDriverLocation() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val latitude = snapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = snapshot.child("longitude").getValue(Double::class.java) ?: 0.0
                    val estimatedTime = snapshot.child("eta").getValue(String::class.java) ?: "N/A"

                    updateLocation(LatLng(latitude, longitude), estimatedTime)
                } else {
                    Toast.makeText(this@TrackDriverActivity, "Driver location not available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TrackDriverActivity, "Failed to read location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateLocation(location: LatLng, estimatedTime: String) {
        driverLocation.text = "Driver's Location: ${location.latitude}, ${location.longitude}"
        eta.text = "Estimated Time of Arrival: $estimatedTime"
        updateMapLocation(location)
    }

    private fun updateMapLocation(location: LatLng) {
        map.clear() // Clear previous markers
        map.addMarker(MarkerOptions().position(location).title("Driver's Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun callDriver() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$driverPhoneNumber")
        }
        startActivity(intent)
    }

    private fun messageDriver() {
        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$driverPhoneNumber")
        }
        startActivity(smsIntent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}
