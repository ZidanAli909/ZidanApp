package com.zidan.zidanapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.google.android.material.snackbar.Snackbar
import com.zidan.zidanapp.databinding.ActivityDiaryCreateBinding
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.jvm.Throws

class DiaryCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryCreateBinding
    private lateinit var toolbar: Toolbar
    private var loadedImage: ByteArray? = null
    private var isClicked = false

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
            toolbar.title = if (isEdit) "Mengubah Diary" else "Membuat Diary"
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
            try {
                // Ambil ukuran file dalam satuan Bytes.
                val inputStream: InputStream = imageUri?.let { contentResolver.openInputStream(it) }!!
                val fileSizeInBytes = inputStream.available()
                inputStream.close()

                // Konversikan ke MegaBytes.
                val fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0)

                // Ambil bitmap serta periksa apakah perlu dirotasi
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                bitmap = rotateImageIfRequired(bitmap, imageUri)

                // Periksa ukuran File
                if (fileSizeInMB > 2.0) {

                    // Jika masih terlalu besar, lakukan kompresi
                    val outputStream = ByteArrayOutputStream()
                    var quality = 90 // Initial quality
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

                    // Turunkan kualitas hingga di bawah 2 MB
                    while (outputStream.toByteArray().size > 2 * 1024 * 1024) {
                        outputStream.reset()
                        quality -= 10
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    }

                    // Konversikan dalam bentuk ByteArray lalu tampilkan gambar hasil kompresi
                    loadedImage = outputStream.toByteArray()
                    binding.apply {
                        Glide.with(this@DiaryCreateActivity).load(loadedImage).into(imageButtonPreviewDiaryMedia)
                    }
                } else {

                    // Gunakan gambar asli, ubah jadi ByteArray, lalu tampilkan
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    loadedImage = stream.toByteArray()
                    binding.apply {
                        Glide.with(this@DiaryCreateActivity).load(loadedImage).into(imageButtonPreviewDiaryMedia)
                    }

                }
                isClicked = false
            } catch (e: IOException) {
                val snackbar = Snackbar.make(
                    binding.root,
                    "Gagal mengambil foto.",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                isClicked = false
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

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    }

    companion object {
        const val EDIT_KEY = "EDIT"
        const val CREATE_KEY = "CREATE"
        const val INTENT_KEY = "EDIT_OR_CREATE"
        const val INTENT_KEY_DIARY_ID = "DIARY_ID"
    }
}