package com.example.kvest2.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskAnswerRelated
//import com.example.kvest2.data.model.AppDataOriginSingleton
import com.example.kvest2.databinding.ChooseServerDialogFragmentBinding
import com.example.kvest2.databinding.UserAnswerTaskDialogBinding
import com.example.kvest2.ui.quest.QuestSharedViewModel
import com.example.kvest2.ui.quest.QuestViewModelFactory

class OfferToAnswerTheQuestionDialogFragment(private val task: TaskAnswerRelated, private val listener: (String) -> Unit): DialogFragment() {

    private lateinit var binding: UserAnswerTaskDialogBinding

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserAnswerTaskDialogBinding.inflate(inflater, container, false)

        initDialogListeners()

        return binding.root
    }


    private fun initDialogListeners() = with(binding) {
        question.text = task.task.question

        dialogBeginBtn.setOnClickListener {
            listener("Hello work, it's my offer on answer")
        }
    }
}