package com.example.yoshi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class VoteFragment : Fragment() {
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

        val documentId = arguments?.getString(ARG_DOCUMENT_ID)
        documentId?.let { fetchDocumentData(it) }
    }

    fun updateVoteData(document: DocumentSnapshot) {
        // Firestore에서 가져온 데이터를 통해 View를 업데이트합니다.
        val voteTitle = view?.findViewById<TextView>(R.id.topic_title)
        val imageUrl = document.getString("image")
        val voteProgress = view?.findViewById<ProgressBar>(R.id.vote_progress)


        // get vote count from the document
        val yesVotes = document.getLong("yesVotes") ?: 0
        val noVotes = document.getLong("noVotes") ?: 0
        val totalVotes = yesVotes + noVotes


        //log image url
        Log.d("VoteFragment", "imageUrl: $imageUrl")

        // val voteDescription = view?.findViewById<TextView>(R.id.tvVoteDescription)
        Log.d("VoteFragment", "DocumentSnapshot data: ${document.data}")
        voteTitle?.text = document.getString("title")

        //Glide.with(this)
        //    .load(imageUrl)
        //    .into(voteImg!!)

        if (totalVotes != 0L) {
            val yesPercentage = (yesVotes.toDouble() / totalVotes.toDouble()) * 100
            voteProgress?.progress = yesPercentage.toInt()
        }

        //voteDescription?.text = document.getString("description")
        // 찬반 투표 옵션에 따라서 추가적인 UI 업데이트가 필요할 수 있습니다.
    }


    fun fetchDocumentData(documentId: String) {
        val db = Firebase.firestore
        db.collection("votes")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    updateVoteData(document)
                }
            }
    }




}