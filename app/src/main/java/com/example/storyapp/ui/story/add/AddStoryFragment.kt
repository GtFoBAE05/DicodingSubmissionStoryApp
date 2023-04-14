package com.example.storyapp.ui.story.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentAddStoryBinding
import com.example.storyapp.utils.Resource
import com.example.storyapp.utils.createCustomTempFile
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class AddStoryFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val cameraPerms = Manifest.permission.CAMERA
    private val galleryPerms = Manifest.permission.READ_EXTERNAL_STORAGE
    private val mapsFinePerms = Manifest.permission.ACCESS_FINE_LOCATION
    private val mapsCoarsePerms = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val addStoryViewModel: AddStoryViewModel by inject()

    private var lat: RequestBody? = null
    private var lon: RequestBody? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setLoading(false)

        checkPermission()

        setupButton()

        setupObserver()

    }

    private fun getLastLocation() {
        if (checkManualPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkManualPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                lat = it.latitude.toString().toRequestBody("text/plain".toMediaType())
                lon = it.longitude.toString().toRequestBody("text/plain".toMediaType())
            }
        } else {
            binding.locationSwitch.isChecked = false
            Toast.makeText(
                requireContext(),
                getString(R.string.location_access_off),
                Toast.LENGTH_SHORT
            ).show()
            checkPermission()
        }


    }

    private fun setupObserver() {
        addStoryViewModel.addResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> setLoading(true)

                is Resource.Error -> {
                    setLoading(false)
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                    view?.findNavController()?.navigate(R.id.action_AddStoryMenu_to_StoryMenu)
                }

                is Resource.Success -> {
                    setLoading(false)
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.success_upload_image),
                        Toast.LENGTH_SHORT
                    ).show()
                    view?.findNavController()?.navigate(R.id.action_AddStoryMenu_to_StoryMenu)
                }
            }
        }
    }

    private fun setupButton() {
        binding.cameraButton.setOnClickListener {
            takePhoto()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLastLocation()
            } else {
                lat = null
                lon = null
            }
        }

        binding.buttonAdd.setOnClickListener {
            if (getFile != null && binding.edAddDescription.text.toString().isNotEmpty()) {
                uploadImage()
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.enter_image_or_description_first),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.uploadPreviewImageView.setImageBitmap(result)
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this.requireContext(),
                "com.example.storyapp.camera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherCamera.launch(intent)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImage, requireActivity())
            getFile = myFile
            binding.uploadPreviewImageView.setImageURI(selectedImage)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.select_image))
        launcherGallery.launch(chooser)
    }

    private fun uploadImage() {
        val file = reduceFileImage(getFile as File)
        val image = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            image
        )

        val desc =
            binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())

        addStoryViewModel.addNewStory(imagePart, desc, lat, lon)


    }

    private fun setLoading(bool: Boolean) {
        binding.addStoryProgressBar.visibility = if (bool) View.VISIBLE else View.GONE
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun checkPermission() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                cameraPerms,
                galleryPerms,
                mapsFinePerms,
                mapsCoarsePerms
            )
        ) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.need_camera_gallery_access),
                1,
                cameraPerms, galleryPerms, mapsFinePerms, mapsCoarsePerms
            )
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun checkManualPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}