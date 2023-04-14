package com.example.storyapp.ui.story.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.data.paging.LoadingStateAdapter
import com.example.storyapp.databinding.FragmentListStoryBinding
import com.example.storyapp.ui.story.detail.DetailStoryFragment
import org.koin.android.ext.android.inject


class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!

    private val listStoryViewModel: ListStoryViewModel by inject()

    private lateinit var listStoryAdapter: ListStoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()

    }

    private fun setupRecyclerView() {
        binding.listStoryRv.layoutManager = LinearLayoutManager(requireContext())
        listStoryAdapter = ListStoryAdapter {
            val bundle = Bundle()
            bundle.putString("storyId", it.id)
            val detailStoryFragment = DetailStoryFragment()
            detailStoryFragment.arguments = bundle
            view?.findNavController()
                ?.navigate(R.id.action_listStoryFragment_to_detailStoryFragment, bundle)
        }

        listStoryAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if(positionStart == 0){
                    binding.listStoryRv.scrollToPosition(0)
                }
            }
        })

        binding.listStoryRv.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            },
        )



    }

    private fun setupObserver() {

        listStoryViewModel.token.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                if (result.isNotEmpty()) {
                    listStoryViewModel.getAllStory(result)
                }
            }
        }

        listStoryViewModel.listStory.observe(viewLifecycleOwner) {
            listStoryAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}