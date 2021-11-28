package com.miloszglowaczewski.tvshowssearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miloszglowaczewski.tvshowssearch.SearchViewModel
import com.miloszglowaczewski.tvshowssearch.TvShowsAdapter
import com.miloszglowaczewski.tvshowssearch.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val listAdapter = TvShowsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.resultList.adapter = listAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SearchState.Data -> {
                            listAdapter.submitList(state.tvShows)
                        }
                        is SearchState.Error -> {
                            listAdapter.submitList(emptyList())
                        }
                        SearchState.Loading -> {
                            listAdapter.submitList(emptyList())
                        }
                        SearchState.WaitingForInput -> {
                            listAdapter.submitList(emptyList())
                        }
                    }.let {}
                }
            }
        }

        binding.searchInput.addTextChangedListener {
            it?.let { editable ->
                viewModel.search(editable.toString())
            }
        }
    }
}