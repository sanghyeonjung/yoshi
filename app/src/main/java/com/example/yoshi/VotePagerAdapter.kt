package com.yoshi.hackatonapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.firebase.firestore.DocumentSnapshot

class VotePagerAdapter(
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
