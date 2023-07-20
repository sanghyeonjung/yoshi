package com.example.yoshi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class VoteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vote_votestart_button = view.findViewById<Button>(R.id.vote_votestart_button)

        vote_votestart_button?.setOnClickListener {
            val intent = Intent(activity, Vote::class.java)
            startActivity(intent)
        }
    }
}
