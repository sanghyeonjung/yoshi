package com.example.yoshi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Vote : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        getAllDocuments { voteDocuments ->
            val viewPager  = findViewById<ViewPager2>(R.id.view_pager)
            viewPager.adapter = VotePagerAdapter(voteDocuments)
            //viewpager 방향 상하로 변경
            viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        }
    }

    inner class VotePagerAdapter(
        private val voteDocuments: List<DocumentSnapshot>
    ) : RecyclerView.Adapter<VotePagerAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Add your view components here
            val topic_title: TextView = itemView.findViewById(R.id.topic_title)
            //val MainimageView: ImageView = itemView.findViewById(R.id.MainimageView)

        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Replace 'my_view' with your layout's resource ID
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_vote, parent, false)
            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            // holder.myTextView.text = myDataset[position]\
            val document = voteDocuments[position]
            holder.topic_title.text = document.getString("title")
            //글라이드 적용
            val imageUrl = document.getString("image")
            Log.d("VoteFragment", "imageUrl: $imageUrl")
            //Glide.with(this@Vote)
            //    .load(imageUrl)
            //    .into(holder.MainimageView)
            //holder.MainimageView.setImageResource(R.drawable.ic_launcher_background)

        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = voteDocuments.size
    }

    fun getAllDocuments(onSuccess: (List<DocumentSnapshot>) -> Unit) {
        db.collection("votes")
            .get()
            .addOnSuccessListener { documents ->
                onSuccess(documents.documents)
                Log.d("Vote", "DocumentSnapshot data: ${documents.documents}")
            }
            .addOnFailureListener { exception ->
                Log.d("Vote", "Error getting documents: ", exception)
            }
    }

}
