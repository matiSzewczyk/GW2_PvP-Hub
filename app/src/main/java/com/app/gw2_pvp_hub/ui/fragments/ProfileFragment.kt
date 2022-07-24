package com.app.gw2_pvp_hub.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.app.gw2_pvp_hub.databinding.FragmentProfileBinding
import com.app.gw2_pvp_hub.ui.viewModels.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val bitmap = when {
                    Build.VERSION.SDK_INT < 28 -> {
                        MediaStore.Images.Media.getBitmap(
                            activity!!.contentResolver,
                            data!!.data
                        )
                    }
                    else -> {
                        val source = ImageDecoder.createSource(
                            activity!!.contentResolver,
                            data?.data!!
                        )
                        ImageDecoder.decodeBitmap(source)
                    }
                }
                viewModel.changePicture(bitmap)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                when (it) {
                    is ProfileViewModel.UiState.ProfileImage -> {
                        binding!!.profilePicture.setImageBitmap(it.bitmap)
                    }
                    else -> Unit
                }
            }
        }

        binding!!.apply {
            profilePicture.setOnClickListener(this@ProfileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}