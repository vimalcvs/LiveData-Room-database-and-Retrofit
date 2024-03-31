package com.vimal.teachers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vimal.teachers.databinding.ActivityMainBinding
import com.vimal.teachers.fragment.FragmentFavorite
import com.vimal.teachers.fragment.FragmentTeachers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this)
        binding.viewPager.setAdapter(viewPagerFragmentAdapter)

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.setText("Teachers")
                1 -> tab.setText("Favourite")
                else -> tab.setText("")
            }
        }.attach()
    }

    private class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentTeachers()
                1 -> FragmentFavorite()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}