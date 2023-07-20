package com.example.yoshi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.yoshi.hackatonapp.MainActivity

class MakeFragment : Fragment() {

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_make, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextTitle = view.findViewById<EditText>(R.id.make_title_edit)

        val radioGroupsubs = view.findViewById<RadioGroup>(R.id.subject_gender_radio)
        val selectedIdSubs = radioGroupsubs.checkedRadioButtonId
        val radioGrouptags = view.findViewById<RadioGroup>(R.id.tag_gender_radio)
        val selectedIdtags = radioGrouptags.checkedRadioButtonId
        //val radioButton = view.findViewById<RadioButton>(selectedIdtags)
        //val selectedText = radioButton.text.toString()
        //val editTextTags = view.findViewById<EditText>(R.id.edit_text_tags)
        var sub = ""

        if(selectedIdSubs == R.id.subject_btn1){
            sub = "MZ"
        }
        else if(selectedIdSubs == R.id.subject_btn2){
            sub = "XY"
        }
        else if(selectedIdSubs == R.id.subject_btn3){
            sub = "SO"
        }

        var tag2 = ""
        if(selectedIdtags == R.id.tag_btn1){
            tag2 = "환경"
        }
        else if(selectedIdtags == R.id.tag_btn2){
            tag2 = "세대"
        }
        else if(selectedIdtags == R.id.tag_btn3){
            tag2 = "인간 관계"
        }




        val editTextReferences = view.findViewById<EditText>(R.id.make_data_edit)
        val editTextDescription = view.findViewById<EditText>(R.id.make_explanation_edit)
        val buttonChooseImage = view.findViewById<Button>(R.id.button_choose_image)
        val buttonSave = view.findViewById<AppCompatButton>(R.id.make_done_btn)

        buttonChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 0)
        }

        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString()
            val subs = sub
            val tags = tag2
            val references = editTextReferences.text.toString()
            val description = editTextDescription.text.toString()

            imageUri?.let { uri ->
                uploadImageToFirebase(uri) { imageUrl ->
                    saveDocument(0,0,title, subs, tags, references, description, imageUrl) {
                        Toast.makeText(context, "Document has been saved!", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDocument(consCount :Int,prosCount:Int,title: String, subs:String, tags: String, references: String, description: String, imageUrl: String, onSuccess: () -> Unit) {
        //uid 가져오기
        val uid = Firebase.auth.currentUser?.uid ?: ""
        //val userId = "test"

        val voteData = hashMapOf(
            "title" to title,
            "tags" to tags,
            "references" to references,
            "description" to description,
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

        db.collection("usersvote").document(uid).collection("votes")
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
