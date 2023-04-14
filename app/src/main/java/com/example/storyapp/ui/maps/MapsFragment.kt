package com.example.storyapp.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentMapsBinding
import com.example.storyapp.utils.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val mapsViewModel: MapsViewModel by inject()

    private val boundsBuilder = LatLngBounds.builder()

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )

        mapsViewModel.token.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                mapsViewModel.getStoryWithMaps(result)
            }
        }


        mapsViewModel.listStory.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val data = it.data.listStory

                    data.forEach { result ->
                        val latlng = LatLng(result.lat, result.lon)
                        googleMap.addMarker(
                            MarkerOptions().position(latlng).title(result.name)
                                .snippet(result.description)
                        )

                        boundsBuilder.include(latlng)


                    }
                    val bounds: LatLngBounds = boundsBuilder.build()

                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )

                }
                is Resource.Error -> {
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                }


            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


}