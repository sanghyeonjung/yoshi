package com.example.yoshi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VoteFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class VoteFragment2 : Fragment() {
    private var vote: String = ""
    companion object {
        private const val ARG_DOCUMENT_ID = "documentId"

        fun newInstance(documentId: String): VoteFragment {
            val args = Bundle()
            args.putString(ARG_DOCUMENT_ID, documentId)

            val fragment = VoteFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //uid 가져오기
        val uid = Firebase.auth.currentUser?.uid ?: ""
        Log.d("VoteFragment", "uid: $uid")
        val documentId = arguments?.getString(ARG_DOCUMENT_ID)
        val yesButton = view.findViewById<TextView>(R.id.left_button)
        val noButton = view.findViewById<TextView>(R.id.right_button)
        yesButton.setOnClickListener {
            vote = "yes"
            Log.d("VoteFragment", "vote: $vote")
            documentId?.let { fetchDocumentData(uid, it) }
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

        var yesVotes = document.getLong("yesVotes") ?: 0
        var noVotes = document.getLong("noVotes") ?: 0
        val totalVotes = yesVotes + noVotes
        //if()

        //check if user has already voted
        val votedUsers = document.get("votedUsers") as? ArrayList<String> ?: ArrayList<String>()

        if (votedUsers.contains(userId)) {
            //user has already voted
            //show a message or do something
        } else {
            //user has not voted yet
            //update the vote count and add the user to the votedUsers list
            votedUsers.add(userId)
            //val vote = // get the vote from user, either "yes" or "no"
                if (vote == "yes") {
                    yesVotes += 1
                }
                else if (vote == "no") {
                    noVotes += 1
                }
                else {
                    Log.d("VoteFragment", "vote: $vote")
                }

            db.collection("votes")
                .document(document.id)
                .update(mapOf("votedUsers" to votedUsers, "yesVotes" to yesVotes, "noVotes" to noVotes))
        }

        voteTitle?.text = document.getString("title")
        Log.d("VoteFragment", "imageUrl: $imageUrl")

        if (totalVotes != 0L) {
            val yesPercentage = (yesVotes.toDouble() / totalVotes.toDouble()) * 100
            voteProgress?.progress = yesPercentage.toInt()
        }
    }

    fun fetchDocumentData(userId: String, documentId: String) {
        val db = Firebase.firestore
        db.collection("votes")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    updateVoteData(userId, document)
                }
            }
    }





}