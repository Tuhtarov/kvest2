package com.example.kvest2.ui.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kvest2.R
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.databinding.QuestUserRecycleViewItemBinding

/**
 * Адаптер для отображения квестов в recycle view
 */
class QuestUserAdapter (
    private val questsUsers: List<QuestUser>,
    private val clickListener: (QuestUser) -> Unit): Adapter<QuestUserAdapter.QuestUserHolder>() {

    class QuestUserHolder(item: View, clickAtPosition: (Int) -> Unit): ViewHolder(item) {
        private val binding = QuestUserRecycleViewItemBinding.bind(item)

        fun bind(questUser: QuestUser) = with(binding) {
            questName.text = questUser.id.toString()
            questDescription.text = questUser.userId.toString()
        }

        init {
            binding.questUserCV.setOnClickListener {
                clickAtPosition(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestUserHolder {
        return QuestUserHolder (
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quest_user_recycle_view_item, parent, false)
        ) {
            clickListener(questsUsers[it])
        }
    }

    override fun onBindViewHolder(holder: QuestUserHolder, position: Int) {
        holder.bind(questsUsers[position])
    }

    override fun getItemCount() = questsUsers.size
}

