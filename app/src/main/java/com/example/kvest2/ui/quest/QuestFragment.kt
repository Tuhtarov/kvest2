package com.example.kvest2.ui.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvest2.R
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
        initFragment()
        showWarningCardViewIfQuestIsEmpty()

        return binding.root
    }

    private fun initFragment() = with(binding) {
        questRecycleView.layoutManager = LinearLayoutManager(context)

        viewModel.questsAvailable.observe(viewLifecycleOwner) {
            questRecycleView.adapter = QuestAdapter(it) { quest ->
                showWarningCardViewIfQuestIsEmpty()
                onClickQuestItem(quest)
            }
        }
    }

    private fun showWarningCardViewIfQuestIsEmpty() = with(binding) {
        if (viewModel.questsAvailable.value?.isEmpty() == true) {
            warnCardView.cardViewMessage.findViewById<TextView>(R.id.textWarningCardView)
                ?.text = getString(R.string.quests_is_empty)

            warnCardView.cardViewMessage.visibility = CardView.VISIBLE
        } else {
            warnCardView.cardViewMessage.visibility = CardView.GONE
        }
    }

    private fun onClickQuestItem(quest: Quest) = with(binding) {
        // добавили квест в пользовательские
        viewModel.appendQuestToUserQuests(quest)

        Toast.makeText(
            context,
            "Выбранный вами квест \"${quest.name}\", доступен для прохождения!",
            Toast.LENGTH_LONG
        ).show()

        // выбрали квест - вернулись обратно
        findNavController().navigateUp()
    }
}
