package com.yoshi.hackatonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoshi.R
import com.example.yoshi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bnv_main = binding.bnvMain as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run {
                setOnItemSelectedListener { it ->
                    when (it.itemId) {
                        R.id.first -> {
                            // 다른 프래그먼트 화면으로 이동하는 기능
                            val homeFragment = HomeFragment()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fl_container, homeFragment).commit()
                        }
                        R.id.second -> {
                            val boardFragment = SleepFragment()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fl_container, boardFragment).commit()
                        }
                        R.id.third -> {
                            val settingFragment = ChattingFragment()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fl_container, settingFragment).commit()
                        }
                        R.id.four -> {
                            val voteFragment = VoteFragment()
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fl_container, voteFragment).commit()

                        }
                    }
                    true
                }
            selectedItemId = R.id.first
        }
    }
}