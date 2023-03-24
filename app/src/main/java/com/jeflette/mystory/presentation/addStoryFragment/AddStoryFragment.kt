package com.jeflette.mystory.presentation.addStoryFragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jeflette.mystory.R
import com.jeflette.mystory.databinding.FragmentAddStoryBinding
import com.jeflette.mystory.util.reduceFileImage
import com.jeflette.mystory.util.rotateBitmap
import com.jeflette.mystory.util.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding
    private val viewModel: AddStoryViewModel by viewModels()

    private var getFile: File? = null
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    context, "Permission tidak diberikan.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding?.apply {
            cameraImageButton.setOnClickListener {
                findNavController().navigate(
                    AddStoryFragmentDirections.actionAddStoryFragmentToCameraFragment()
                )
            }
            galleryImageButton.setOnClickListener { startGallery() }
            buttonAdd.setOnClickListener { uploadImage() }
        }

        setFragmentListener()
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                getString(R.string.this_is_image_description).toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            addStories(imageMultipart, description)
            getAddStoryResult()
        } else {
            Toast.makeText(
                requireContext(), getString(R.string.please_select_image_first), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getAddStoryResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addStoryResult.collectLatest { result ->
                    Log.d("AddStoryFragment", "getAddStoryResult: ${result.data?.message}")
                    if (result.data?.error == true) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.failed_add_story),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.success_add_story),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun addStories(
        imageMultipart: MultipartBody.Part, description: RequestBody
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tokenResult.collectLatest { token ->
                    viewModel.addStory(token, imageMultipart, description)
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.select_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = result.data?.data?.let { uriToFile(it, requireContext()) }
            binding?.imagePreview?.let { Glide.with(this).load(myFile).into(it) }
        }
    }

    private fun setFragmentListener() {
        setFragmentResultListener(CAMERA_X_RESULT) { _, bundle ->
            val myFile = bundle.getSerializable("fileResult") as File
            val isBackCamera = bundle.getBoolean("isBackCamera", true)
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path), isBackCamera
            )
            binding?.imagePreview?.let { Glide.with(this).load(result).into(it) }

        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = "200"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}