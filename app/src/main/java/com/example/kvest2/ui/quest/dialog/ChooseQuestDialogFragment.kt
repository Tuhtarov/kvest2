package com.example.kvest2.ui.quest.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.kvest2.data.entity.QuestUserRelated
import com.example.kvest2.databinding.ChooseQuestDialogFragmentBinding
import com.example.kvest2.ui.quest.QuestSharedViewModel
import com.example.kvest2.ui.quest.QuestViewModelFactory

class ChooseQuestDialogFragment(private val questUserRelated: QuestUserRelated): DialogFragment() {

    private lateinit var binding: ChooseQuestDialogFragmentBinding

    private val viewModel: QuestSharedViewModel by activityViewModels {
        QuestViewModelFactory(binding.root.context)
    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseQuestDialogFragmentBinding.inflate(inflater, container, false)

        binding.apply {
            val quest = questUserRelated.quest

            questTitle.text = quest.name
            questDescription.text = quest.description
            questCreatedAt.text = quest.createdAt
        }

        initDialogListeners()

        return binding.root
    }

    private fun initDialogListeners() = with(binding) {
        val isCurrent = questUserRelated.questUser.isCurrent

        dialogCloseBtn.setOnClickListener {
            dismiss()
        }

        questChooseBtn.setOnClickListener {
            viewModel.changeQuestStatus(questUserRelated, true)
            dismiss()
        }

        questCancelBtn.setOnClickListener {
            viewModel.changeQuestStatus(questUserRelated, false)
            dismiss()
        }

        questChooseBtn.visibility = if (isCurrent) Button.GONE else Button.VISIBLE
        questCancelBtn.visibility = if (isCurrent) Button.VISIBLE else Button.GONE
    }
}