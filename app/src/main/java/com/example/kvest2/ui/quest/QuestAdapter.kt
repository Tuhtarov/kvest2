package com.example.kvest2.ui.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.kvest2.R
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.databinding.QuestRecycleViewItemBinding

/**
 * Адаптер для отображения квестов в recycle view
 */
class QuestAdapter(private val quests: List<Quest>): Adapter<QuestAdapter.QuestHolder>() {
    class QuestHolder(item: View): ViewHolder(item) {
        private val binding = QuestRecycleViewItemBinding.bind(item)

        fun bind(quest: Quest) = with(binding) {
            questName.text = quest.name
            questDescription.text = quest.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.quest_recycle_view_item, parent, false)

        return QuestHolder(view)
    }

    override fun onBindViewHolder(holder: QuestHolder, position: Int) {
        holder.bind(quests[position])
    }

    override fun getItemCount(): Int {
        return quests.size
    }
}

