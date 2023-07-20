package com.yoshi.hackatonapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.yoshi.R
import com.example.yoshi.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.yoshi.hackatonapp.MainActivity

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    lateinit var mAuth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val items = resources.getStringArray(R.array.my_array)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        val spinner = binding.signupAgeSpinner
        spinner.adapter = myAdapter
        //인증 초기화
        mAuth = Firebase.auth
        binding.signupSignUpBtn.setOnClickListener {

            val email = binding.signupEmailEdit.text.toString().trim()
            val password = binding.signupPasswordEdit.text.toString().trim()

            signUp(email, password)
        }
    }

    /**
     * 회원 가입
     */
    private fun signUp(email: String, password: String) {
        val name = binding.signupNameEdit.text.toString().trim()
        val email = binding.signupEmailEdit.text.toString().trim()
        val password = binding.signupPasswordEdit.text.toString().trim()
        var gender : String = ""
        if (binding.rgBtn1.isChecked)
        {
            gender = "남자"
        }
        else
        {
            gender = "여자"
        }
        val age = binding.signupAgeSpinner.selectedItem.toString()
        var m : String = ""
        var b : String = ""
        var t : String = ""
        var i : String = ""
        if(binding.rgEIBtn1.isChecked)
        {
            m = "E"
        }
        else
        {
            m = "I"
        }
        if(binding.rgNSBtn1.isChecked)
        {
            b = "N"
        }
        else
        {
            b = "S"
        }
        if(binding.rgFTBtn1.isChecked)
        {
            t = "F"
        }
        else
        {
            t = "T"
        }
        if(binding.rgPJBtn1.isChecked)
        {
            i = "P"
        }
        else
        {
            i = "J"
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let { currentUser ->
                        val userMap = hashMapOf<String, String>(
                            "name" to name,
                            "email" to email,
                            "gender" to gender,
                            "age" to age,
                            "m" to m,
                            "b" to b,
                            "t" to t,
                            "i" to i
                        )
                        db.collection("user")
                            .document(currentUser.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                val intent: Intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                startActivity(intent)
                                // 저장 성공 처리
                                // 예를 들어, 회원가입 성공 후 메인 액티비티로 이동하는 코드를 추가할 수 있습니다.
                            }
                            .addOnFailureListener { e ->
                                Log.e("Storage", "Error : ${e}")
                                // 저장 실패 처리
                            }
                    }
                    // 성공시 실행
                } else {
                    // 실패시 실행
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    Log.d("SignUp", "Error: ${task.exception}")
                }
            }
    }

}