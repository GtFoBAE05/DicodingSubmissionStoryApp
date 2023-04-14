package com.example.storyapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.storyapp.databinding.FragmentProfileBinding
import com.example.storyapp.ui.login.MainActivity
import org.koin.android.ext.android.inject


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupButton()

    }

    private fun setupObserver() {
        profileViewModel.getId().observe(viewLifecycleOwner) {
            binding.idTv.text = it
        }

        profileViewModel.getName().observe(viewLifecycleOwner) {
            binding.nameTv.text = it
        }
    }

    private fun setupButton() {
        binding.logoutButton.setOnClickListener {
            profileViewModel.removeAll()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}