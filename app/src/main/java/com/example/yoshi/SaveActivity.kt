package com.example.yoshi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class SaveActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val editTextTitle = findViewById<EditText>(R.id.edit_text_title)
        val buttonSave = findViewById<Button>(R.id.button_save)
        val buttonChooseImage = findViewById<Button>(R.id.button_choose_image)

        buttonChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 0)
        }

        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString()
            imageUri?.let { uri ->
                uploadImageToFirebase(uri) { imageUrl ->
                    saveDocument(title, imageUrl) {
                        Toast.makeText(this, "Document has been saved!", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDocument(title: String, imageUrl: String, onSuccess: () -> Unit) {
        val voteData = hashMapOf(
            "title" to title,
            "image" to imageUrl
        )

        db.collection("votes")
            .add(voteData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("SaveActivity", "Error adding document", e)
            }
    }

    private fun uploadImageToFirebase(uri: Uri, onSuccess: (imageUrl: String) -> Unit) {
        val ref: StorageReference = storage.reference.child("uploads/" + System.currentTimeMillis())
        val uploadTask = ref.putFile(uri)

        val urlTask: Task<Uri> = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                onSuccess(downloadUri.toString())
            } else {
                // Handle failures
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
        }
    }
}