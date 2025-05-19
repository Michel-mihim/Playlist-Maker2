package com.example.playlist_maker2.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.TracksAdapter
import com.example.playlist_maker2.databinding.FragmentSearchBinding
import com.example.playlist_maker2.search.domain.models.SearchActivityState
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.constants.Constants.CLICK_DEBOUNCE_DELAY
import com.example.playlist_maker2.utils.constants.Constants.SEARCH_DEF
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding

    //инициализированные объекты====================================================================
    private var isClickAllowed = true

    //не инициализированные объекты=================================================================
    private lateinit var inputManager: InputMethodManager

    private val searchViewModel by viewModel<SearchViewModel>()

    private val adapter = TracksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.observeSearchActivityState().observe(viewLifecycleOwner) {
            render(it)
        }

        searchViewModel.observeSearchActivityToastState().observe(viewLifecycleOwner) { message ->
            showToast(message)
        }

        searchViewModel.observePlayerActivityIntent().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        binding.searchResultsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager пока не нужен

        searchViewModel.showHistory()

        //слушатели=================================================================================

        adapter.onItemClickListener = { track ->
            if (clickDebouncer()) {
                //запуск плеера
                searchViewModel.getPlayerIntent(track)
                searchViewModel.writeHistory(track)
            }
        }


        binding.historyClearButton.setOnClickListener{
            searchViewModel.clearHistory()
        }


        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.setText(SEARCH_DEF)

        }

        binding.searchRenewButton.setOnClickListener {
            if (clickDebouncer()) renewRequest()
        }


        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearButton.visibility = searchClearButtonVisibility(s)
                searchViewModel.searchDelayed(
                    changedText = s?.toString() ?: ""
                )

                if (s.isNullOrEmpty()) {
                    searchViewModel.showHistory()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.searchEditText.addTextChangedListener(textWatcher)

    }

    private fun renewRequest(){
        searchViewModel.searchForce(binding.searchEditText.text.toString())
    }

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    //управление видимостью=========================================================================
    private fun render(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.Loading -> showLoading()
            is SearchActivityState.Default -> showDefault()
            is SearchActivityState.Empty -> showEmpty()
            is SearchActivityState.Content -> showContent(state.tracks)
            is SearchActivityState.Error -> showError()
            is SearchActivityState.History -> showHistory(state.tracks)
        }
    }

    private fun showLoading() {
        defaultViewsShow()
        showSearchProgressbar()
    }

    private fun showDefault() {
        defaultViewsShow()
    }

    private fun showHistory(tracks: List<Track>) {
        historyViewsShow()

        binding.historyRecycler.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        contentViewsShow()

        binding.searchResultsRecycler.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        emptyViewsShow(Constants.TRACKS_NOT_FOUND, R.drawable.not_found)
    }

    private fun showError() {
        errorViewsShow(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSearchProgressbar(){
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideSearchProgressbar(){
        binding.searchProgressBar.visibility = View.INVISIBLE
    }

    private fun defaultViewsShow() {
        hideSearchProgressbar()
        binding.searchResultsRecycler.visibility = View.INVISIBLE
        binding.historyRecycler.visibility = View.INVISIBLE
        binding.searchRenewButton.visibility = View.INVISIBLE
        binding.historyClearButton.visibility = View.INVISIBLE
        hidePlaceholder()
        binding.youFoundText.visibility = View.INVISIBLE
    }

    private fun historyViewsShow() {
        hideSearchProgressbar()
        binding.searchResultsRecycler.visibility = View.INVISIBLE
        binding.historyRecycler.visibility = View.VISIBLE
        binding.searchRenewButton.visibility = View.INVISIBLE
        binding.historyClearButton.visibility = View.VISIBLE
        hidePlaceholder()
        binding.youFoundText.visibility = View.VISIBLE
    }

    private fun emptyViewsShow(text: String, image: Int) {
        hideSearchProgressbar()
        binding.searchResultsRecycler.visibility = View.INVISIBLE
        binding.historyRecycler.visibility = View.INVISIBLE
        binding.searchRenewButton.visibility = View.INVISIBLE
        binding.historyClearButton.visibility = View.INVISIBLE
        showPlaceholder(text, image)
        binding.youFoundText.visibility = View.INVISIBLE
    }

    private fun contentViewsShow() {
        hideSearchProgressbar()
        binding.searchResultsRecycler.visibility = View.VISIBLE
        binding.historyRecycler.visibility = View.INVISIBLE
        binding.searchRenewButton.visibility = View.INVISIBLE
        binding.historyClearButton.visibility = View.INVISIBLE
        hidePlaceholder()
        binding.youFoundText.visibility = View.INVISIBLE
    }

    private fun errorViewsShow(text: String, image: Int) {
        hideSearchProgressbar()
        binding.searchResultsRecycler.visibility = View.INVISIBLE
        binding.historyRecycler.visibility = View.INVISIBLE
        binding.searchRenewButton.visibility = View.VISIBLE
        binding.historyClearButton.visibility = View.INVISIBLE
        showPlaceholder(text, image)
        binding.youFoundText.visibility = View.INVISIBLE
    }

    private fun showPlaceholder(text: String, image: Int) {
        binding.placeholderText.text = text
        binding.placeholderPic.setImageResource(image)
        binding.placeholderText.visibility = View.VISIBLE
        binding.placeholderPic.visibility = View.VISIBLE
    }

    private fun hidePlaceholder() {
        binding.placeholderText.visibility = View.INVISIBLE
        binding.placeholderPic.visibility = View.INVISIBLE
    }

    private fun searchClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

}