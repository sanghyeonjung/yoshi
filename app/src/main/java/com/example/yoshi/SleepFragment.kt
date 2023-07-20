package com.example.yoshi

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yoshi.databinding.FragmentSleepBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SleepFragment : Fragment() {
    private var _binding: FragmentSleepBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepBinding.inflate(inflater, container, false)
        val view = binding.root

        fun loadVotes(onSuccess: (List<VoteData>) -> Unit) {
            val db = Firebase.firestore
            val uid = Firebase.auth.currentUser?.uid ?: ""
            if (uid != null) {
                db.collection("uesrsvote").document(uid).collection("votes")
                    .get()
                    .addOnSuccessListener { documents ->
                        val voteList = documents.map {
                            it.toObject(VoteData::class.java)
                        }
                        onSuccess(voteList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
            }
            else{
                Log.w(TAG, "Error getting documents.")
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerView)

        loadVotes { votes ->
            recyclerView.adapter = VoteAdapter(votes)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}