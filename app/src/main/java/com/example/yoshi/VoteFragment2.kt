package com.example.yoshi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [VoteFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class VoteFragment2 : Fragment() {
    private var vote: String = ""
    companion object {
        private const val ARG_DOCUMENT_ID = "documentId"

        fun newInstance(documentId: String): VoteFragment2 {
            val args = Bundle()
            args.putString(ARG_DOCUMENT_ID, documentId)

            val fragment = VoteFragment2()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = Firebase.auth.currentUser?.uid ?: ""
        Log.d("VoteFragment", "uid: $uid")
        val documentId = arguments?.getString(ARG_DOCUMENT_ID)
        val yesButton = view.findViewById<ImageButton>(R.id.left_button2)
        val noButton = view.findViewById<ImageButton>(R.id.right_button2)

        yesButton.setOnClickListener {
            vote = "yes"
            Log.d("VoteFragment", "vote: $vote")
            documentId?.let { fetchDocumentData(uid, it) }
            //documentid log
            Log.d("VoteFragment2", "documentId: $documentId")

        }
        noButton.setOnClickListener {
            vote = "no"
            Log.d("VoteFragment", "vote: $vote")
            documentId?.let { fetchDocumentData(uid, it) }
        }
        //documentId?.let { fetchDocumentData(uid, it) }
    }

    fun updateVoteData(userId: String, document: DocumentSnapshot) {
        val db = Firebase.firestore
        val voteTitle = view?.findViewById<TextView>(R.id.topic_title)
        val imageUrl = document.getString("image")
        val voteProgress = view?.findViewById<ProgressBar>(R.id.vote_progress)

        var yesVotes = document.getLong("consCount") ?: 0
        var noVotes = document.getLong("prosCount") ?: 0
        val totalVotes = yesVotes + noVotes

        //check if user has already voted
        val votedUsers = document.get("votedUsers") as? ArrayList<String> ?: ArrayList<String>()

        if (votedUsers.contains(userId)) {
            //user has already voted
            //show a message or do something
        } else {
            //user has not voted yet
            //update the vote count and add the user to the votedUsers list
            votedUsers.add(userId)

            if (vote == "yes") {
                yesVotes += 1
            }
            else if (vote == "no") {
                noVotes += 1
            }

            val newTotalVotes = yesVotes + noVotes
            db.collection("votes")
                .document(document.id)
                .update(mapOf("votedUsers" to votedUsers, "consCount" to yesVotes, "prosCount" to noVotes))

            if (newTotalVotes != 0L) {
                val yesPercentage = (yesVotes.toDouble() / newTotalVotes.toDouble()) * 100
                voteProgress?.progress = yesPercentage.toInt()
            }
        }

        voteTitle?.text = document.getString("title")
        Log.d("VoteFragment", "imageUrl: $imageUrl")
    }


    fun fetchDocumentData(userId: String, documentId: String) {
        val db = Firebase.firestore
        db.collection("votes")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    updateVoteData(userId, document)
                    Log.d("VoteFragment2", "DocumentSnapshot data: ${document.data}")
                }
            }
    }





}