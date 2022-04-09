package com.example.kvest2.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.kvest2.data.model.AppDataOriginSingleton
import com.example.kvest2.databinding.ChooseServerDialogFragmentBinding
import com.example.kvest2.ui.quest.QuestSharedViewModel
import com.example.kvest2.ui.quest.QuestViewModelFactory

class ChooseServerDialogFragment: DialogFragment() {

    private lateinit var binding: ChooseServerDialogFragmentBinding

    private val viewModel: QuestSharedViewModel by activityViewModels {
        QuestViewModelFactory(binding.root.context)
    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseServerDialogFragmentBinding.inflate(inflater, container, false)
        isCancelable = false

        initDialogListeners()

        return binding.root
    }

    private fun initDialogListeners() = with(binding) {
        onDismiss(this)

        acceptBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun onDismiss(dialog: ChooseServerDialogFragmentBinding) {
        if (binding.radioButtonCustom.isActivated) {
            AppDataOriginSingleton.chooseCustom (
                host = binding.serverHost.text.toString(),
                port = binding.serverPort.text.toString()
            )
        } else if (binding.radioButtonRemote.isActivated) {
            AppDataOriginSingleton.chooseRemote()
        }
    }
}