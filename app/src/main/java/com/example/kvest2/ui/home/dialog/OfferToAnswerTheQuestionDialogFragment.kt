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

class OfferToAnswerTheQuestionDialogFragment: DialogFragment() {

    private lateinit var binding: UserAnswerTaskDialogBinding

    private val viewModel: QuestSharedViewModel by activityViewModels {
        QuestViewModelFactory(binding.root.context)

    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserAnswerTaskDialogBinding.inflate(inflater, container, false)

        initDialogListeners()

        return binding.root
    }
<<<<<<< Updated upstream
     fun show(manager: FragmentManager, tag: String?, task: TaskAnswerRelated, listener: (String) -> Unit) {

        //binding.question.text =  task.task.question
=======
>>>>>>> Stashed changes


     fun show(manager: FragmentManager, tag: String?, task: Task, listener: (String) -> Unit) {
//        binding.question.text =  task.question

        listener("Ответ")
    }


    private fun initDialogListeners() = with(binding) {

    }
}