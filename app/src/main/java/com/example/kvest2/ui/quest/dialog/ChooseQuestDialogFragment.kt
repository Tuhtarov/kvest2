package com.example.kvest2.ui.quest.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.databinding.ChooseQuestDialogFragmentBinding

class ChooseQuestDialogFragment (
    private val questUser: QuestUser,
    private val onChooseListener: () -> Unit
): DialogFragment() {

    private lateinit var binding: ChooseQuestDialogFragmentBinding

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseQuestDialogFragmentBinding.inflate(inflater, container, false)

        binding.dialogCloseBtn.setOnClickListener {
            dismiss()
        }

        if (questUser.quest != null) {
            showQuestData(questUser.quest!!)
        } else {
            val toastMessage = "Закрепленный квест не был получен для отображения модального окна"
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    private fun showQuestData(quest: Quest) = with(binding) {
        questTitle.text = quest.name
        questDescription.text = quest.description
        questCreatedAt.text = quest.createdAt

        questChooseBtn.setOnClickListener {
            onChooseListener()
            dismiss()
        }
    }
}