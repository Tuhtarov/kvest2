package com.example.kvest2.ui.quest.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.kvest2.data.entity.Quest
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

        binding.dialogCloseBtn.setOnClickListener {
            dismiss()
        }

        binding.questChooseBtn.setOnClickListener {
            viewModel.changeQuestStatus(questUserRelated, true)
            dismiss()
        }

        binding.questCancelBtn.setOnClickListener {
            viewModel.changeQuestStatus(questUserRelated, false)
            dismiss()
        }

        if (questUserRelated.questUser.isCurrent) {
            binding.questChooseBtn.visibility = Button.GONE
            binding.questCancelBtn.visibility = Button.VISIBLE
        } else {
            binding.questChooseBtn.visibility = Button.VISIBLE
            binding.questCancelBtn.visibility = Button.GONE
        }

        showQuestData(questUserRelated.quest)

        return binding.root
    }

    private fun showQuestData(quest: Quest) = with(binding) {
        questTitle.text = quest.name
        questDescription.text = quest.description
        questCreatedAt.text = quest.createdAt
    }
}