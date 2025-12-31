package com.example.myapp017christmasapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp017christmasapp.data.GiftsPreferencesRepository
import com.example.myapp017christmasapp.databinding.FragmentGiftsOverviewBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GiftsOverviewFragment : Fragment() {

    private var _binding: FragmentGiftsOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: GiftsPreferencesRepository
    private lateinit var adapter: GiftsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiftsOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = GiftsPreferencesRepository(requireContext())

        binding.recyclerViewGifts.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            combine(
                repository.momGiftFlow,
                repository.dadGiftFlow,
                repository.siblingGiftFlow,
                repository.catGiftFlow,
                repository.dogGiftFlow
            ) { mom, dad, sibling, cat, dog ->
                listOf(
                    PersonGift("Mamka", mom),
                    PersonGift("Táta", dad),
                    PersonGift("Sourozenec", sibling),
                    PersonGift("Kočka", cat),
                    PersonGift("Pes", dog)
                )
            }.collect { list ->
                adapter = GiftsAdapter(list)
                binding.recyclerViewGifts.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
