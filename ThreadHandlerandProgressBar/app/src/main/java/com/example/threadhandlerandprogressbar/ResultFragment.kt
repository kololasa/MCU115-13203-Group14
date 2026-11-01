package com.example.threadhandlerandprogressbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ResultFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var tvResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        tvResult = view.findViewById(R.id.tvResult)

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                "PREPARING" -> tvResult.text = "準備中"
                "WORKING" -> tvResult.text = "工作中"
                "FINISHED" -> tvResult.text = "背景工作結朿"
                "IDLE", "CANCELLED" -> tvResult.text = if (status == "CANCELLED") "已中斷" else ""
            }
        }
    }
}