package com.example.kvest2.ui.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.databinding.QuestFragmentBinding

/**
 * Для страницы доступных квестов (те, что приходят от сервера)
 */
class QuestFragment : Fragment() {
    private lateinit var binding: QuestFragmentBinding

    private val viewModel: QuestSharedViewModel by activityViewModels {
        QuestViewModelFactory(binding.root.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuestFragmentBinding.inflate(inflater, container, false)

        binding.questRecycleView.layoutManager = LinearLayoutManager(context)

        viewModel.quests.observe(viewLifecycleOwner) {
            onClickQuestItem(it)
        }

        return binding.root
    }

    private fun onClickQuestItem(listOfQuests: MutableList<Quest>) {
        binding.questRecycleView.adapter = QuestAdapter(listOfQuests) { quest ->
            viewModel.appendQuestToUserQuestsIfNotExists(quest)
            findNavController().navigateUp()
        }
    }
}
