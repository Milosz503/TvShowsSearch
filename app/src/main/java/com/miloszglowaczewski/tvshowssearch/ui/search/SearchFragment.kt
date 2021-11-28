package com.miloszglowaczewski.tvshowssearch.ui.search

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
import com.miloszglowaczewski.tvshowssearch.TvShowModel
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
                            if(state.tvShows.isEmpty()) {
                                showInfoResult("No results")
                            } else {
                                showItems(state.tvShows)
                            }

                        }
                        is SearchState.Error -> {
                            showInfoResult(state.error)
                        }
                        SearchState.Loading -> {
                            showInfoResult("Loading...")
                        }
                        SearchState.WaitingForInput -> {
                            showInfoResult("Start typing to see results")
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

    private fun showInfoResult(info: String) {
        listAdapter.submitList(emptyList())
        binding.resultText.text = info

        binding.resultList.visibility = View.INVISIBLE
        binding.resultText.visibility = View.VISIBLE
    }

    private fun showItems(items: List<TvShowModel>) {
        listAdapter.submitList(items)
        binding.resultText.text = ""

        binding.resultList.visibility = View.VISIBLE
        binding.resultText.visibility = View.INVISIBLE
    }
}