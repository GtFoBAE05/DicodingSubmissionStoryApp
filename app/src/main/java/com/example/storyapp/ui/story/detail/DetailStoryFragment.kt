package com.example.storyapp.ui.story.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.FragmentDetailStoryBinding
import com.example.storyapp.utils.Resource
import org.koin.android.ext.android.inject


class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    private val detailStoryViewModel: DetailStoryViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val id = bundle?.getString("storyId")

        detailStoryViewModel.getDetailStory(id.toString())
        setupObserver()

    }

    private fun setupObserver() {
        detailStoryViewModel.detailStory.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> setLoading(true)

                is Resource.Success -> {
                    setLoading(false)
                    Glide.with(requireContext()).load(it.data.story.photoUrl)
                        .into(binding.ivDetailPhoto)
                    binding.tvDetailName.text = it.data.story.name
                    binding.tvDetailDescription.text = it.data.story.description
                }

                is Resource.Error -> {
                    setLoading(false)
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLoading(bool: Boolean) {
        binding.detailStoryProgressBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}