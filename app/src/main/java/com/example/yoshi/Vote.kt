package com.yoshi.hackatonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.yoshi.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Vote : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllDocuments { voteDocuments ->
            val pagerAdapter = VotePagerAdapter(supportFragmentManager, voteDocuments)

            val viewPager = findViewById<ViewPager>(R.id.view_pager)
            viewPager.adapter = pagerAdapter
        }
    }

    inner class VotePagerAdapter(
        fragmentManager: FragmentManager,
        private val voteDocuments: List<DocumentSnapshot>
    ) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = voteDocuments.size

        override fun getItem(position: Int): Fragment {
            val fragment = VoteFragment.newInstance()
            fragment.updateVoteData(voteDocuments[position])
            return fragment
        }
    }

    fun getAllDocuments(onSuccess: (List<DocumentSnapshot>) -> Unit) {
        db.collection("votes")
            .get()
            .addOnSuccessListener { documents ->
                onSuccess(documents.documents)
            }
    }
}
