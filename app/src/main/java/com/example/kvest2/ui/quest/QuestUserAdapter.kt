package com.example.kvest2.ui.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kvest2.R
import com.example.kvest2.data.entity.QuestUserRelated
import com.example.kvest2.databinding.QuestUserRecycleViewItemBinding

/**
 * Адаптер для отображения квестов в recycle view
 */
class QuestUserAdapter (
    private val questsUsersRelated: List<QuestUserRelated>,
    private val colorCurrentQuestResId: Int? = null,
    private val clickListener: (QuestUserRelated) -> Unit,
): Adapter<QuestUserAdapter.QuestUserHolder>() {

    class QuestUserHolder (
        private val colorCurrentQuestResId: Int? = null,
        item: View,
        clickAtPosition: (Int) -> Unit,
    ): ViewHolder(item) {
        private val binding = QuestUserRecycleViewItemBinding.bind(item)

        fun bind(questUserRelated: QuestUserRelated) = with(binding) {
            questName.text = questUserRelated.quest.name
            questDescription.text = questUserRelated.quest.description

            if (questUserRelated.questUser.isCurrent && colorCurrentQuestResId != null) {
               questUserCV.setCardBackgroundColor(colorCurrentQuestResId)
            }
        }

        init {
            binding.questUserCV.setOnClickListener {
                clickAtPosition(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestUserHolder {
        return QuestUserHolder (
            colorCurrentQuestResId,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quest_user_recycle_view_item, parent, false)
        ) {
            clickListener(questsUsersRelated[it])
        }
    }

    override fun onBindViewHolder(holder: QuestUserHolder, position: Int) {
        holder.bind(questsUsersRelated[position])
    }

    override fun getItemCount() = questsUsersRelated.size
}

