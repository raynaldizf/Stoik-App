package com.app.stoikapp.view.history.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.stoikapp.view.history.HistoryBookingFragment
import com.app.stoikapp.view.history.HistoryDiagnosisFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryBookingFragment()
            1 -> HistoryDiagnosisFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
