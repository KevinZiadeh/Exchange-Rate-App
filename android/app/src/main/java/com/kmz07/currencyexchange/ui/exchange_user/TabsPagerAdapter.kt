package com.kmz07.currencyexchange.ui.exchange_user

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    var getExchangesFragmentRef: GetExchanges? = null
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DoExchange()
            }
            1 -> {
                getExchangesFragmentRef = GetExchanges()
                return getExchangesFragmentRef as GetExchanges
            }
            else -> DoExchange()
        }
    }
    override fun getItemCount(): Int {
        return 2
    }
}