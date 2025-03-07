package com.zidan.zidanapp.Fragments.Main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zidan.zidanapp.Activities.DiaryCreateActivity
import com.zidan.zidanapp.Activities.DiaryDetailsActivity
import com.zidan.zidanapp.Adapters.DiaryAdapter
import com.zidan.zidanapp.Adapters.LoadingStateAdapter
import com.zidan.zidanapp.Data.Models.Diary
import com.zidan.zidanapp.ViewModels.DiaryViewModel
import com.zidan.zidanapp.databinding.FragmentMainHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val adapter = DiaryAdapter()
        adapter.onItemClickListener = this@MainHomeFragment
        with(binding) {
            recyclerViewDiaryList.apply {
                this.adapter = adapter
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
        diaryViewModel.diaryPagedList.observe(viewLifecycleOwner) { pagedList ->
            adapter.submitList(pagedList)
        }
    }

    override fun onItemClick(item: Diary) {
        val intent = Intent(requireContext(), DiaryDetailsActivity::class.java)
        intent.putExtra(DiaryCreateActivity.INTENT_KEY_DIARY_ID, item.id)
        startActivity(intent)
    }
}