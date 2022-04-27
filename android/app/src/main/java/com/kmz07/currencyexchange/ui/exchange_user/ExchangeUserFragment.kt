package com.kmz07.currencyexchange.ui.exchange_user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kmz07.currencyexchange.databinding.FragmentExchangeUserBinding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ExchangeUserFragment : Fragment() {
    private var _binding: FragmentExchangeUserBinding? = null
    private val binding get() = _binding!!
    private var tabLayout: TabLayout? = null
    private var tabsViewPager: ViewPager2? = null
    private var adapter: TabsPagerAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExchangeUserBinding.inflate(inflater, container, false)
        val root: View = binding.root


        tabLayout = binding.exchangeUserTabLayout
        tabsViewPager = binding.exchangeUserTabsViewPager
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        tabLayout?.isInlineLabel = true
        // Enable Swipe
        tabsViewPager?.isUserInputEnabled = true
        // Set the ViewPager Adapter
        adapter = TabsPagerAdapter(childFragmentManager, lifecycle)
        tabsViewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, tabsViewPager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Exchange with User"
                }
                1 -> {
                    tab.text = "Get My Exchanges"
                }
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}