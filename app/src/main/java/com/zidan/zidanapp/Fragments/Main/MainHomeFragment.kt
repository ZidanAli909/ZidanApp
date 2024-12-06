package com.zidan.zidanapp.Fragments.Main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zidan.zidanapp.Activities.DiaryCreateActivity
import com.zidan.zidanapp.Activities.DiaryDetailsActivity
import com.zidan.zidanapp.Adapters.DiaryAdapter
import com.zidan.zidanapp.Adapters.LoadingStateAdapter
import com.zidan.zidanapp.Data.Model.Diary
import com.zidan.zidanapp.ViewModel.DiaryViewModel
import com.zidan.zidanapp.databinding.FragmentMainHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainHomeFragment : Fragment(), DiaryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMainHomeBinding

    private lateinit var fAB_add: FloatingActionButton

    private val diaryViewModel by viewModels<DiaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        fAB_add = binding.fABAddDiary
        fAB_add.setOnClickListener {
            val intent = Intent(requireActivity(), DiaryCreateActivity::class.java)
            startActivity(intent)
        }

        initViews()
        return binding.root
    }

    private fun initViews() {
        with(binding) {
            // Initialize adapter
            val diaryAdapter = DiaryAdapter()
            diaryAdapter.onItemClickListener = this@MainHomeFragment

            recyclerViewDiaryList.apply {
                setHasFixedSize(true)
                recyclerViewDiaryList.layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = diaryAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        diaryAdapter.retry()
                    }
                )
            }

            lifecycleScope.launch {
                diaryViewModel.diaryPagingData.collectLatest { pagingData ->
                    diaryAdapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onItemClick(item: Diary) {
        val intent = Intent(requireContext(), DiaryDetailsActivity::class.java)
        intent.putExtra(DiaryCreateActivity.INTENT_KEY_DIARY_ID, item.id)
        startActivity(intent)
    }
}