package com.yoshi.hackatonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yoshi.databinding.FragmentSleepBinding
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
        val getTimeBtn = binding.sleepGettimeButton
        val showBeforeTimeText = binding.sleepShowbeforetimeText
        var start : Long = 0

        Log.e("시간","suc")
        getTimeBtn.setOnClickListener{
            showBeforeTimeText.text = ""
            Log.e("버튼안","suc")
            start = System.currentTimeMillis()
            val date = Date(start)
            val mFormat = SimpleDateFormat("HH시mm분") // 현재 시간
                //현재 시간 + 13분이 자는 시간
            val time = mFormat.format(date)
            Log.e("time : ",time)
            for(i: Int in 1..6)
            {

            }
            showBeforeTimeText.text = time
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}