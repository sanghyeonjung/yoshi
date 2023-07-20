package com.yoshi.hackatonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yoshi.R
import com.google.firebase.firestore.DocumentSnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VoteFragment : Fragment() {
    companion object {
        fun newInstance(): VoteFragment {
            return VoteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun updateVoteData(document: DocumentSnapshot) {
        // Firestore에서 가져온 데이터를 통해 View를 업데이트합니다.
        val voteTitle = view?.findViewById<TextView>(R.id.topic_title)
       // val voteDescription = view?.findViewById<TextView>(R.id.tvVoteDescription)

        voteTitle?.text = document.getString("title")
        //voteDescription?.text = document.getString("description")
        // 찬반 투표 옵션에 따라서 추가적인 UI 업데이트가 필요할 수 있습니다.
    }
}