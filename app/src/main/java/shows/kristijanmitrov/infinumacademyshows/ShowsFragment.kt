package shows.kristijanmitrov.infinumacademyshows

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import shows.kristijanmitrov.infinumacademyshows.databinding.DialogProfileBinding
import shows.kristijanmitrov.infinumacademyshows.databinding.FragmentShowsBinding
import shows.kristijanmitrov.model.User
import shows.kristijanmitrov.networking.ApiModule
import shows.kristijanmitrov.ui.ShowsAdapter
import shows.kristijanmitrov.viewModel.ShowsViewModel

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private var latestTmpUri: Uri? = null
    private val viewModel by viewModels<ShowsViewModel>()
    private lateinit var bottomSheetBinding: DialogProfileBinding
    private lateinit var adapter: ShowsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                user.imageUrl = uri.toString()
                sharedPreferences.edit{putString(Constants.IMAGE, user.imageUrl)}
                viewModel.onProfilePhotoChanged(uri)

            }
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            user.imageUrl = uri.toString()
            sharedPreferences.edit{putString(Constants.IMAGE, user.imageUrl)}
            viewModel.onProfilePhotoChanged(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(Constants.LOGIN_PREFERENCES, Context.MODE_PRIVATE)

        val id = sharedPreferences.getString(Constants.ID, null)
        val email = sharedPreferences.getString(Constants.EMAIL, null)
        val imageUrl = sharedPreferences.getString(Constants.IMAGE, null)

        if(id == null || email == null){
            val directions = ShowsFragmentDirections.toLoginFragment()
            findNavController().navigate(directions)
        }else user = User(id, email, imageUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiModule.initRetrofit(requireContext())

        //Observers
        viewModel.profilePhoto.observe(viewLifecycleOwner){ profilePhoto ->
            binding.profilePhoto.setImageURI(profilePhoto)
            bottomSheetBinding.profilePhoto.setImageURI(profilePhoto)
        }

        viewModel.getShowsResultLiveData().observe(viewLifecycleOwner){ ShowsResponse ->
            if(ShowsResponse.isSuccessful){
                ShowsResponse.body?.let {
                    adapter.setShows(it.shows)
                    Toast.makeText(requireContext(), "Shows set", Toast.LENGTH_SHORT).show()
                }
            }else
                Toast.makeText(requireContext(), "Shows not set", Toast.LENGTH_SHORT).show()
        }

        initToolbar()
        initShowRecycler()
        initShowHideButton()
    }

    private fun initToolbar() {
        user.imageUrl?.let{
            val profilePhotoUri = Uri.parse(user.imageUrl)
            binding.profilePhoto.setImageURI(profilePhotoUri)
        }

        binding.profilePhoto.setOnClickListener {
            showProfileBottomSheet()
        }
    }

    private fun showProfileBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        bottomSheetBinding = DialogProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        //init information
        bottomSheetBinding.email.text = user.email
        user.imageUrl?.let{
            val profilePhotoUri = Uri.parse(user.imageUrl)
            bottomSheetBinding.profilePhoto.setImageURI(profilePhotoUri)
        }

        //init change profile photo
        bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.change_profile_photo))
                .setPositiveButton(getString(R.string.open_camera)) { _, _ ->
                    if (checkCameraPermission()) {
                        takeImage()
                    } else {
                        requestCameraPermission()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.choose_from_gallery)) { _, _ ->
                    if (checkReadStoragePermission()) {
                        selectImageFromGallery()
                    } else {
                        requestReadStoragePermission()
                    }
                    dialog.dismiss()
                }
                .show()
        }

        //init logout
        bottomSheetBinding.logoutButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.logging_out))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    sharedPreferences.edit {
                        remove(Constants.REMEMBER_ME)
                        remove(Constants.ID)
                        remove(Constants.IMAGE)
                        remove(Constants.IMAGE)
                        remove(Constants.ACCESS_TOKEN)
                        remove(Constants.CLIENT)
                        remove(Constants.EXPIRY)
                        remove(Constants.UID)
                    }
                    dialog.dismiss()
                    findNavController().navigate(ShowsFragmentDirections.toLoginFragment())
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .show()
        }

        //init close icon
        bottomSheetBinding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile(Constants.IMAGE, ".png", requireContext().filesDir).apply {
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
    private fun requestReadStoragePermission() {
        requestReadStoragePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    //Check Permission
    private fun checkCameraPermission() =
        ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
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

        val accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, null)
        val client = sharedPreferences.getString(Constants.CLIENT, null)
        val expiry = sharedPreferences.getString(Constants.CLIENT, null)
        val uid = sharedPreferences.getString(Constants.UID, null)

        if(accessToken == null || client == null || expiry == null || uid == null){
            val directions = ShowsFragmentDirections.toLoginFragment()
            findNavController().navigate(directions)
        }else viewModel.init(accessToken, client, expiry, uid)
    }

    private fun initShowHideButton() = with(binding) {
        binding.showHideButton.setOnClickListener {
            if (adapter.itemCount == 0) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}