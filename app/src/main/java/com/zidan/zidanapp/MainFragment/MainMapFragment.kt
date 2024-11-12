package com.zidan.zidanapp.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zidan.zidanapp.R
import com.zidan.zidanapp.databinding.FragmentMainMapBinding

class MainMapFragment : Fragment() {
    private lateinit var binding: FragmentMainMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMapBinding.inflate(inflater, container, false)
        return binding.root
    }
}