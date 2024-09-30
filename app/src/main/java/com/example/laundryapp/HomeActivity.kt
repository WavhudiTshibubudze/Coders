package com.example.laundryapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var orderSummaryListView: ListView
    private lateinit var notificationsListView: ListView
    private lateinit var map: GoogleMap
    private lateinit var database: DatabaseReference

    // Sample data for order summary and notifications
    private val orderSummaryData = mutableListOf("Order 1: 3 Items", "Order 2: 5 Items")
    private val notificationsData = mutableListOf("Pickup Reminder", "Order Delivered")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("drivers")

        // Initialize UI components
        orderSummaryListView = findViewById(R.id.lvOrderSummary)
        notificationsListView = findViewById(R.id.lvNotifications)

        // Populate order summary and notifications
        populateOrderSummary()
        populateNotifications()

        // Handle Add Slip button click
        findViewById<Button>(R.id.btnAddSlip).setOnClickListener {
            addSlip()
        }

        // Handle Remove Slip button click
        findViewById<Button>(R.id.btnRemoveSlip).setOnClickListener {
            removeSlip()
        }

        // Initialize the Google Maps fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Fetch driver's location from Firebase
        fetchDriverLocation()
    }

    // Function to populate the order summary list
    private fun populateOrderSummary() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderSummaryData)
        orderSummaryListView.adapter = adapter
    }

    // Function to populate the notifications list
    private fun populateNotifications() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notificationsData)
        notificationsListView.adapter = adapter
    }

    // Function to add a new slip
    private fun addSlip() {

        orderSummaryData.add("Order ${orderSummaryData.size + 1}: 4 Items")
        (orderSummaryListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        Toast.makeText(this, "Slip Added", Toast.LENGTH_SHORT).show()
    }

    // Function to remove the last slip
    private fun removeSlip() {
        if (orderSummaryData.isNotEmpty()) {
            orderSummaryData.removeAt(orderSummaryData.size - 1)
            (orderSummaryListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            Toast.makeText(this, "Slip Removed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No Slips to Remove", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch the driver's location from Firebase
    private fun fetchDriverLocation() {
        database.child("driver_id_1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val latitude = snapshot.child("latitude").getValue(Double::class.java) ?: return
                    val longitude = snapshot.child("longitude").getValue(Double::class.java) ?: return
                    updateDriverLocation(LatLng(latitude, longitude))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Failed to load driver location: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Update the map with the driver's location
    private fun updateDriverLocation(driverLocation: LatLng) {
        map.clear() // Clear existing markers
        map.addMarker(MarkerOptions().position(driverLocation).title("Driver Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 12f))
    }

    // Google Maps setup
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        fetchDriverLocation()
    }
}
