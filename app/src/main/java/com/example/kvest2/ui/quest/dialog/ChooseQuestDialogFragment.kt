package com.example.kvest2.ui.quest.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.databinding.ChooseQuestDialogFragmentBinding

class ChooseQuestDialogFragment (
    private val quest: Quest,
    private val onChooseListener: () -> Unit
): DialogFragment() {
    private lateinit var binding: ChooseQuestDialogFragmentBinding

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseQuestDialogFragmentBinding.inflate(inflater, container, false)

        binding.apply {
            questTitle.text = quest.name
            questDescription.text = quest.description
            questCreatedAt.text = quest.createdAt

            dialogCloseBtn.setOnClickListener {
                dismiss()
            }

            questChooseBtn.setOnClickListener {
                onChooseListener()
                dismiss()
            }
        }

        return binding.root
    }
}