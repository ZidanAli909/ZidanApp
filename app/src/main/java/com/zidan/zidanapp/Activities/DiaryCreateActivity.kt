package com.zidan.zidanapp.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.zidan.zidanapp.Data.Models.Diary
import com.zidan.zidanapp.Media.*
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModels.DiaryViewModel
import com.zidan.zidanapp.databinding.ActivityDiaryCreateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.IOException
import java.io.InputStream

@AndroidEntryPoint
class DiaryCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryCreateBinding
    private lateinit var toolbar: Toolbar
    private var loadedImage: ByteArray? = null
    private var isClicked = false
    private val diaryViewModel by viewModels<DiaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryCreateBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    private fun initViews() {
        // Toolbar
        toolbar = binding.diaryCreateToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Initialize
        val isEdit = if (intent.getStringExtra(INTENT_KEY) == EDIT_KEY) true else false
        with(binding) {
            var diaryData: Diary? = null
            val diaryId = intent.getIntExtra(INTENT_KEY_DIARY_ID, -1)
            if (isEdit) {
                diaryViewModel.getDiaryById(diaryId)
                lifecycleScope.launch {
                    diaryViewModel.stateGetDiary.collectLatest {
                        diaryData = it
                        editTextDiaryTitle.setText(diaryData?.title)
                        editTextDiaryDescription.setText(diaryData?.description)
                        Glide.with(this@DiaryCreateActivity).load(diaryData?.media).into(imageButtonPreviewDiaryMedia)
                        loadedImage = diaryData?.media
                    }
                }
            }

            buttonConfirm.setOnClickListener {
                val title = if (editTextDiaryTitle.text.isNullOrEmpty()) "Tanpa Judul" else editTextDiaryTitle.text.toString()
                val description = if (editTextDiaryDescription.text.isNullOrEmpty()) "Tanpa deskripsi" else editTextDiaryDescription.text.toString()
                diaryViewModel.createDiary(
                    Diary(
                        diaryData?.id ?: 0,
                        title,
                        description,
                        loadedImage
                    )
                )
                val toastTxt = if (isEdit) "Sukses mengubah diary!" else "Sukses membuat diary!"
                Toast.makeText(this@DiaryCreateActivity, toastTxt, Toast.LENGTH_SHORT).show()
                finish()
            }

            buttonDelete.setOnClickListener {
                val dialog = MaterialAlertDialogBuilder(this@DiaryCreateActivity)
                dialog.setTitle(resources.getString(R.string.dialog_title_warning))
                dialog.setMessage(resources.getString(R.string.dialog_delete_description))
                dialog.setPositiveButton(resources.getString(R.string.dialog_choice_yes)) { dialog, which ->
                    diaryData.apply {
                        diaryViewModel.deleteDiary(this!!)
                    }
                    Toast.makeText(this@DiaryCreateActivity, "Sukses menghapus diary!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                dialog.setNegativeButton(resources.getString(R.string.dialog_choice_no)) { dialog, which ->
                    dialog.dismiss()
                }
                dialog.show()
            }

            toolbar.title = if (isEdit) "Mengubah Diary" else "Membuat Diary"
            buttonDelete.visibility = if (isEdit) View.VISIBLE else View.GONE
            imageButtonPreviewDiaryMedia.setOnClickListener { openGallery() }
        }
    }

    private fun openGallery() {
        if (!isClicked) {
            isClicked = true
            val intent = Intent()
            intent.setType("image/*")
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                try {
                    // Ambil bitmap
                    var bitmap = getScaledBitmap(imageUri, this, 1024, 1024) // Di-scaled awal untuk mencegah memori berlebih
                    bitmap = rotateImageIfRequired(bitmap, imageUri) // Cek jika perlu di-rotate
                    loadedImage = compressImage(bitmap) // Kompresi
                    binding.apply {
                        Glide.with(this@DiaryCreateActivity).load(loadedImage).into(imageButtonPreviewDiaryMedia)
                    }
                    isClicked = false
                } catch (e: IOException) {
                    val snackbar = Snackbar.make(
                        binding.root, "Gagal mengambil foto!", Snackbar.LENGTH_SHORT
                    ).show()
                    isClicked = false
                } catch (e: OutOfMemoryError) {
                    Snackbar.make(
                        binding.root, "Foto terlalu besar untuk diproses!", Snackbar.LENGTH_SHORT
                    ).show()
                    isClicked = false
                }
            }
        }
        isClicked = false
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap {
        val input: InputStream? = contentResolver.openInputStream(selectedImage)
        val ei = ExifInterface(input!!)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    companion object {
        const val EDIT_KEY = "EDIT"
        const val CREATE_KEY = "CREATE"
        const val INTENT_KEY = "EDIT_OR_CREATE"
        const val INTENT_KEY_DIARY_ID = "DIARY_ID"
    }
}