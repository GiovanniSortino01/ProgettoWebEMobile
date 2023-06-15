package com.example.progettowebemobile.principale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentSearchBinding
import com.example.progettowebemobile.principale.search.HotelFragment
import com.example.progettowebemobile.principale.search.MonumentFragment
import com.example.progettowebemobile.principale.search.RestaurantFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtnRestaurant.setOnClickListener {
            val restaurantFragment = RestaurantFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.search_flFragment, restaurantFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.searchBtnHotel.setOnClickListener {
            val hotelFragment = HotelFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.search_flFragment, hotelFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.searchBtnMonuments.setOnClickListener {
            val monumentFragment = MonumentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.search_flFragment, monumentFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}