package com.example.kvest2.ui.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kvest2.R
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.databinding.QuestRecycleViewItemBinding

/**
 * Адаптер для отображения квестов в recycle view
 */
class QuestAdapter (
    private val quests: List<Quest>,
    private val clickListener: (Quest) -> Unit): Adapter<QuestAdapter.QuestHolder>() {

    class QuestHolder(item: View, clickAtPosition: (Int) -> Unit): ViewHolder(item) {
        private val binding = QuestRecycleViewItemBinding.bind(item)

        fun bind(quest: Quest) = with(binding) {
            questName.text = quest.name
            questDescription.text = quest.description
        }

        init {
            binding.questCV.setOnClickListener {
                clickAtPosition(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestHolder {
        return QuestHolder (
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quest_recycle_view_item, parent, false)
        ) {
            clickListener(quests[it])
        }
    }

    override fun onBindViewHolder(holder: QuestHolder, position: Int) {
        holder.bind(quests[position])
    }

    override fun getItemCount() = quests.size
}

