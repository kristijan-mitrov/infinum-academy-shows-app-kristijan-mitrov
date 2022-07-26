package shows.kristijanmitrov.infinumacademyshows

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.InputStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogProfileBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentShowsBinding
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.ui.ShowsAdapter

private const val REMEMBER_ME = "REMEMBER_ME"
private const val USER = "USER"
private const val CAMERA_REQUEST_CODE = 0

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBinding: DialogProfileBinding
    private lateinit var adapter: ShowsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User
    private var latestTmpUri: Uri? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.profilePhoto.setImageURI(uri)
                bottomSheetBinding.profilePhoto.setImageURI(uri)
            }
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.profilePhoto.setImageURI(uri)
            bottomSheetBinding.profilePhoto.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        user = sharedPreferences.getUser(USER, null)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initShowRecycler()
        initShowHideButton()
    }

    private fun initToolbar() {
        binding.profilePhoto.setBackgroundResource(user.profilePhoto)

        binding.profilePhoto.setOnClickListener {

            showProfileBottomSheet()
        }
    }

    private fun showProfileBottomSheet() {
        val dialog = context?.let { BottomSheetDialog(it) }

        bottomSheetBinding = DialogProfileBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetBinding.root)

        //init information
        bottomSheetBinding.email.text = user.email
        bottomSheetBinding.profilePhoto.setBackgroundResource(user.profilePhoto)

        //init change profile photo
        bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.change_profile_photo))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.open_camera)) { _, _ ->
                    // TODO: Take photo from camera
                    if (checkCameraPermission()) {
                        takeImage()
                    } else {
                        requestCameraPermission()
                    }
                }
                .setNegativeButton(getString(R.string.choose_from_gallery)) { _, _ ->
                    // TODO: Take photo from gallery
                    if (checkReadStoragePermission()) {
                        selectImageFromGallery()
                    } else {
                        requestReadStoragePermission()
                    }
                }
                .show()
        }

        //init logout
        bottomSheetBinding.logoutButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.logging_out))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    sharedPreferences.edit {
                        putBoolean(REMEMBER_ME, false)
                        remove(USER)
                    }
                    dialog?.dismiss()
                    findNavController().navigate(ShowsFragmentDirections.toLoginFragment())
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .show()
        }

        //init close icon
        bottomSheetBinding.closeIcon.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("profilePhoto", ".png", requireContext().filesDir).apply {
            createNewFile()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    //Request Permission Launcher

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            takeImage()
        } else {
            Toast.makeText(context, getString(R.string.permission_must_be_granted_to_use_the_camera_app), Toast.LENGTH_SHORT).show()
        }
    }

    private val requestWriteStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission has been granted. Start camera preview Activity.
            } else {
                // Permission request was denied.
            }
        }

    private val requestReadStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            selectImageFromGallery()
        } else {
            Toast.makeText(context, getString(R.string.permission_must_be_granted_to_open_the_gallery), Toast.LENGTH_SHORT).show()
        }
    }

    //Request Permission
    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun requestWriteStoragePermission() {
        requestWriteStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun requestReadStoragePermission() {
        requestReadStoragePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    //Check Permission
    private fun checkCameraPermission() =
        ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun checkWriteStoragePermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkReadStoragePermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun initShowRecycler() {
        adapter = ShowsAdapter { show ->
            val directions = ShowsFragmentDirections.toShowDetailsFragment(show)
            findNavController().navigate(directions)
        }

        binding.showsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.showsRecycler.adapter = adapter

        adapter.setShows()
    }

    private fun initShowHideButton() = with(binding) {
        binding.showHideButton.setOnClickListener {
            if (adapter.itemCount == 0) {
                adapter.setShows()
                showsRecycler.isVisible = true
                emptyStateLayout.isVisible = false
                showHideButton.text = getString(R.string.hide)
            } else {
                adapter.clearShows()
                showsRecycler.isVisible = false
                emptyStateLayout.isVisible = true
                showHideButton.text = getString(R.string.show)
            }
        }
    }

    private fun SharedPreferences.getUser(s: String, default: String?): User? {
        val userJson = getString(s, default)
        return userJson?.let { Json.decodeFromString(userJson) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}