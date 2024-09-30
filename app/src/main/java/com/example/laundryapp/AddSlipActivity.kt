package com.example.laundryapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AddSlipActivity : AppCompatActivity() {

    private lateinit var editSlipNumber: EditText
    private lateinit var editClothingType: EditText
    private lateinit var editMaterial: EditText
    private lateinit var editColor: EditText
    private lateinit var editDescription: EditText
    private lateinit var imagePreview: ImageView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slip)

        editSlipNumber = findViewById(R.id.edit_slip_number)
        editClothingType = findViewById(R.id.edit_clothing_type)
        editMaterial = findViewById(R.id.edit_material)
        editColor = findViewById(R.id.edit_color)
        editDescription = findViewById(R.id.edit_description)
        imagePreview = findViewById(R.id.image_preview)

        // Button to upload image
        findViewById<Button>(R.id.button_upload_image).setOnClickListener {
            openImagePicker()
        }

        // Button to add item
        findViewById<Button>(R.id.button_add_item).setOnClickListener {
            addItem()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imagePreview.setImageURI(imageUri) // Display the selected image
            imagePreview.visibility = ImageView.VISIBLE // Make the ImageView visible
        }
    }

    private fun addItem() {
        val slipNumber = editSlipNumber.text.toString()
        val clothingType = editClothingType.text.toString()
        val material = editMaterial.text.toString()
        val color = editColor.text.toString()
        val description = editDescription.text.toString()

        if (slipNumber.isEmpty() || clothingType.isEmpty() || material.isEmpty() ||
            color.isEmpty() || description.isEmpty() || imageUri == null
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a ClothingItem object
        val newItem = ClothingItem(
            slipNumber,
            clothingType,
            color,
            material,
            imageUri.toString(),
            description
        )
        saveItemToDatabase(newItem) // Save to Firebase
    }

    private fun saveItemToDatabase(item: ClothingItem) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("clothing_items")


        val itemId = myRef.push().key

        // Check if the itemId is not null
        if (itemId != null) {
            // Save the item to the database
            myRef.child(itemId).setValue(item)
                .addOnSuccessListener {
                    // Item saved successfully
                    Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show()
                    clearInputFields() // Clear the input fields after saving
                }
                .addOnFailureListener { e ->
                    // Failed to save item
                    Toast.makeText(this, "Failed to save item: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error saving item", e) // Log error
                }
        }
    }

    private fun clearInputFields() {
        editSlipNumber.text.clear()
        editClothingType.text.clear()
        editMaterial.text.clear()
        editColor.text.clear()
        editDescription.text.clear()
        imagePreview.setImageDrawable(null)
        imageUri = null
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val TAG = "AddSlipActivity"
    }
}

data class ClothingItem(
    val slipNumber: String,
    val type: String,
    val color: String,
    val material: String,
    val imageUri: String,
    val description: String
)
