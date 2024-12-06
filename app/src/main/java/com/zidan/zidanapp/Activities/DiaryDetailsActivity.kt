package com.zidan.zidanapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zidan.zidanapp.Activities.DiaryCreateActivity.Companion
import com.zidan.zidanapp.Data.Model.Diary
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModel.DiaryViewModel
import com.zidan.zidanapp.databinding.ActivityDiaryDetailsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DiaryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryDetailsBinding
    private var loadedImage: ByteArray? = null
    private var isClicked = false
    private val diaryViewModel by viewModels<DiaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailsBinding.inflate(layoutInflater)
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
        with(binding) {
            // Loading current diary
            var diaryData: Diary? = null
            val diaryId = intent.getIntExtra(DiaryCreateActivity.INTENT_KEY_DIARY_ID, -1)
            diaryViewModel.getDiaryById(diaryId)
            lifecycleScope.launch {
                diaryViewModel.stateGetDiary.collectLatest {
                    diaryData = it
                    textViewTitle.text = diaryData?.title
                    textViewDescription.text = diaryData?.description
                    Glide.with(this@DiaryDetailsActivity).load(diaryData?.media).into(imageViewMedia)
                    loadedImage = diaryData?.media
                }
            }
            // Back Button
            buttonBack.setOnClickListener {
                onBackPressed()
            }
            // Edit Button
            buttonEdit.setOnClickListener {
                val intent = Intent(this@DiaryDetailsActivity, DiaryCreateActivity::class.java)
                intent.putExtra(INTENT_KEY_DIARY_ID, diaryData?.id)
                intent.putExtra(DiaryCreateActivity.INTENT_KEY, DiaryCreateActivity.EDIT_KEY)
                startActivity(intent)
            }
            // Delete Button
            buttonDelete.setOnClickListener {
                val dialog = MaterialAlertDialogBuilder(this@DiaryDetailsActivity)
                dialog.setTitle(resources.getString(R.string.dialog_title_warning))
                dialog.setMessage(resources.getString(R.string.dialog_delete_description))
                dialog.setPositiveButton(resources.getString(R.string.dialog_choice_yes)) { dialog, which ->
                    diaryData.apply {
                        diaryViewModel.deleteDiary(this!!)
                    }
                    finish()
                }
                dialog.setNegativeButton(resources.getString(R.string.dialog_choice_no)) { dialog, which ->
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    companion object {
        const val INTENT_KEY_DIARY_ID = "DIARY_ID"
    }
}