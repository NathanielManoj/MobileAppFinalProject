package com.nathanielmanoj.mobileappfinalproject


import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var createButton: Button
    private lateinit var dateButton: ImageButton
    private lateinit var locationButton: ImageButton
    private var eventDate: String = ""
    private var eventLocation: String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_event)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        createButton = findViewById(R.id.createButton)
        dateButton = findViewById(R.id.dateButton)
        locationButton = findViewById(R.id.locationButton)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        locationButton.setOnClickListener {
            requestLocationPermission()
        }

        createButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty() && eventDate.isNotEmpty() && eventLocation.isNotEmpty()) {
                createEvent(title, description, eventDate, eventLocation)
            } else {
                Toast.makeText(this, "Please fill out all fields, select a date and location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, {
            _, selectedYear, selectedMonth, selectedDay ->
            eventDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            Toast.makeText(this, "Selected date: $eventDate", Toast.LENGTH_SHORT).show()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // This check is redundant, as it's already handled in requestLocationPermission, but required by the IDE.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    eventLocation = "${location.latitude}, ${location.longitude}"
                    Toast.makeText(this, "Location selected: $eventLocation", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createEvent(title: String, description: String, date: String, location: String) {
        val database = FirebaseDatabase.getInstance()
        val eventsRef = database.getReference("events")
        val eventId = eventsRef.push().key

        if (eventId != null) {
            val event = hashMapOf(
                "title" to title,
                "description" to description,
                "date" to date,
                "location" to location
            )
            eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener {
                    Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show()
                }
        }
    }
}