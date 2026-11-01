package com.example.threadhandlerandprogressbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var tvProgressPercent: TextView
    private lateinit var tvStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        progressBar = view.findViewById(R.id.progressBar)
        tvProgressPercent = view.findViewById(R.id.tvProgressPercent)
        tvStatus = view.findViewById(R.id.tvStatus)

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                "PREPARING" -> {
                    tvStatus.text = "準備中"
                    progressBar.progress = 0
                    tvProgressPercent.text = "0%"
                }
                "WORKING" -> {
                    tvStatus.text = "" // 隱藏狀態文字，僅顯示進度條和中間的 %
                }
                "FINISHED" -> {
                    tvStatus.text = "背景工作結朿"
                    progressBar.progress = 100
                    tvProgressPercent.text = "100%"
                }
                "IDLE", "CANCELLED" -> {
                    tvStatus.text = if (status == "CANCELLED") "已中斷" else ""
                }
            }
        }

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            if (viewModel.status.value == "WORKING") {
                progressBar.progress = progress
                tvProgressPercent.text = "$progress%"
            }
        }
    }
}