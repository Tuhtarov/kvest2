package com.example.kvest2.ui.task.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvest2.data.entity.*
import com.example.kvest2.databinding.ChooseTaskDialogFragmentBinding
import com.example.kvest2.ui.task.TaskAdapter

class ChooseTaskDialogFragment(
    private val currentQuest: Quest,
    private val tasksUser: List<TaskUserRelated>,
    private val tasksAnswers: List<TaskAnswerRelated>,
    private val chooseListener: (TaskAnswerRelated) -> Unit
) : DialogFragment() {
    private lateinit var binding: ChooseTaskDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChooseTaskDialogFragmentBinding.inflate(inflater, container, false)

        dialog
            ?.window
            ?.setBackgroundDrawableResource(android.R.color.transparent)

        val tasksImproved = getTasksImproved()

        binding.apply {
            questTitle.text = currentQuest.name
            questDescription.text = currentQuest.description

            taskRv.layoutManager = LinearLayoutManager(requireContext())

            // инициализация прослушки и recycle view
            taskRv.adapter = TaskAdapter(tasksImproved) { response ->
                chooseListener(response.taskAnswer)
                dismiss()
            }
        }

        initDialogListeners()

        return binding.root
    }

    private fun getTasksImproved(): List<TaskUserWithAnswerRelated> {
        val tasksImproved = mutableListOf<TaskUserWithAnswerRelated>()

        tasksAnswers.forEach { taskAnswer ->
            val taskImproved = TaskUserWithAnswerRelated(taskAnswer)

            tasksUser.forEach { taskUser ->
                if (taskAnswer.task.id == taskUser.task.id) {
                    taskImproved.taskUser = taskUser
                }
            }

            tasksImproved.add(taskImproved)
        }

        return tasksImproved
    }

    private fun initDialogListeners() = with(binding) {
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    data class TaskUserWithAnswerRelated (
        val taskAnswer: TaskAnswerRelated,
        var taskUser: TaskUserRelated? = null
    )
}