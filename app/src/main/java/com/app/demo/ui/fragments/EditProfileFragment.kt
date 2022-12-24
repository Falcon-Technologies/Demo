package com.app.demo.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.app.demo.ApplicationClass
import com.app.demo.R
import com.app.demo.databinding.FragmentEditProfileBinding
import com.app.demo.model.Owner
import com.app.demo.ui.fragments.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@Suppress("DEPRECATION")
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    // A global variable for stored image path.
    private var mImagePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return getPersistentView(inflater, container, R.layout.fragment_edit_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()

        ownerViewModel.allOwnerList.observe(viewLifecycleOwner) { owner ->
            if (owner.isEmpty()) {
                return@observe
            }

            owner[0].apply {
                binding.etOwnerName.setText(name)
                Glide.with(requireContext()).load(image)
                    .into(binding.changePicture)
                binding.etOwnerAge.setText(age.toString())
            }
        }
    }


    private fun listener() {
        binding.changePicture.setOnClickListener {
            Dexter.withContext(requireContext()).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    // Here after all the permission are granted launch the gallery to select and image.
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        requireContext(),
                        "You have denied the storage permission to select image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?, token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()

        }

        binding.btnSave.setOnClickListener {

            if (binding.etOwnerAge.text.isEmpty() || binding.etOwnerName.text.isEmpty()) {
                showMessage("Field is empty")
                return@setOnClickListener
            } else if (mImagePath == "") {
                showMessage("You have to update image before save")
                return@setOnClickListener
            }
            val owner = Owner(
                id = 1,
                name = binding.etOwnerName.text.toString(),
                image = mImagePath,
                age = binding.etOwnerAge.text.toString().toInt()
            )
            try {
                val entry = Owner(1, "Muzammil Hussain", mImagePath, 23)
                ownerViewModel.insertOwner(owner = entry)
                ownerViewModel.updateOwner(owner)
            } catch (e: Exception) {
                showMessage(
                    "owner not updated. make sure you added valid entry.",
                    Toast.LENGTH_LONG
                )

            }

            if (findNavController().currentDestination?.id == R.id.editProfileFragment) {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
     */
    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireContext())
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {

                data?.let {
                    // Here we will get the select image URI.
                    val selectedPhotoUri = data.data

                    // Set Selected Image URI to the imageView using Glide
                    Glide.with(requireContext()).load(selectedPhotoUri).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                // log exception
                                Log.e("TAG", "Error loading image", e)
                                return false // important to return false so the error placeholder can be placed
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {

                                val bitmap: Bitmap = resource.toBitmap()

                                mImagePath = saveImageToInternalStorage(bitmap)
                                Log.i("ImagePath", mImagePath)
                                return false
                            }
                        }).into(binding.changePicture)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): String {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(ApplicationClass.getContext())

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         * File creation mode: the default mode, where the created file can only
         * be accessed by the calling application (or all applications sharing the
         * same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Mention a file name to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image absolute path
        return file.absolutePath
    }

    companion object {
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "OwnerImage"

    }
}
