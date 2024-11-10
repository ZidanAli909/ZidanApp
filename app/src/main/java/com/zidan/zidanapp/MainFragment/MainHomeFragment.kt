package com.zidan.zidanapp.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModel.TimeSpentViewModel
import com.zidan.zidanapp.databinding.FragmentMainHomeBinding

class MainHomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding

    private lateinit var liveTimeSpentViewModel: TimeSpentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        liveTimeSpentViewModel = ViewModelProvider(this)[TimeSpentViewModel::class.java]
        updateTimeObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updateTimeObserver() {
        val elapsedTimeObserver = Observer<Long?> { aLong ->
            val newText = this@MainHomeFragment.resources.getString(R.string.timeSpent, aLong)
            binding.HomeTextViewTimeSpent.text = newText
        }
        liveTimeSpentViewModel.getElapsedTime().observe(this, elapsedTimeObserver)
    }
}