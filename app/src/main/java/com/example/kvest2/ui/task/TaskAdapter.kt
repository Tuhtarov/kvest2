package com.example.kvest2.ui.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kvest2.R
import com.example.kvest2.databinding.TaskRvItemBinding
import com.example.kvest2.ui.task.dialog.ChooseTaskDialogFragment as taskDialog

/**
 * Адаптер для отображения квестов в recycle view
 */
class TaskAdapter (
    private val tasks: List<taskDialog.TaskUserWithAnswerRelated>,
    private val clickListener: (taskDialog.TaskUserWithAnswerRelated) -> Unit): Adapter<TaskAdapter.TaskHolder>() {

    class TaskHolder(item: View, clickAtPosition: (Int) -> Unit): ViewHolder(item) {
        private val binding = TaskRvItemBinding.bind(item)

        fun bind(task: taskDialog.TaskUserWithAnswerRelated) = with(binding) {
            taskTitle.text = "Задание #$bindingAdapterPosition"

            Log.i("current-task-recycle-view", "${task.taskAnswer.task.question} текущая задача? = ${task.taskUser?.taskUser?.isCurrent}")

            if (task.taskUser?.taskUser?.isCurrent == true) {
                taskTitle.text = taskTitle.text.toString().plus(" (текущее)")
            }
        }

        init {
            binding.taskRvItem.setOnClickListener {
                clickAtPosition(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder (
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_rv_item, parent, false)
        ) {
            clickListener(tasks[it])
        }
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size
}

