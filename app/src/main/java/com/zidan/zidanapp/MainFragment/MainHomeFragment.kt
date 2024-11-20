package com.zidan.zidanapp.MainFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zidan.zidanapp.DiaryCreateActivity
import com.zidan.zidanapp.DiaryDetailsActivity
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModel.TimeSpentViewModel
import com.zidan.zidanapp.databinding.FragmentMainHomeBinding

class MainHomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding //Binding

    private lateinit var fAB_add: FloatingActionButton

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
            val intent = Intent(requireActivity(),DiaryCreateActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}