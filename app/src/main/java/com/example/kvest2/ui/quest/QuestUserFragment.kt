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
import com.example.kvest2.data.entity.QuestUserRelated
import com.example.kvest2.databinding.QuestUserFragmentBinding
import com.example.kvest2.ui.quest.dialog.ChooseQuestDialogFragment

/**
 * Для страницы пользовательских квестов (тех, что выбрал пользователь) для их прохождения
 */
class QuestUserFragment : Fragment() {
    private lateinit var binding: QuestUserFragmentBinding

    private val viewModel: QuestSharedViewModel by activityViewModels {
        QuestViewModelFactory(binding.root.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuestUserFragmentBinding.inflate(inflater, container, false)

        initFragment()
        showWarningCardViewIfQuestIsEmpty()

        return binding.root
    }

    /**
     * Инициализация списка отображения пользовательских квестов и обработчиков событий
     */
    private fun initFragment() = with(binding) {
        questsUserRecycleView.layoutManager = LinearLayoutManager(context)

        viewModel.questUserRelated.observe(viewLifecycleOwner) {
            showWarningCardViewIfQuestIsEmpty()

            val currentQuestColor = R.color.quest_user_item_current

            questsUserRecycleView.adapter = QuestUserAdapter(it, currentQuestColor) { quest ->
                onClickQuestUserItem(quest)
            }

            if (it.isEmpty()) {
                Toast.makeText(context, "У вас нет добавленных квестов", Toast.LENGTH_LONG)
                    .show()
            }
        }

        btnForAddQuest.setOnClickListener {
            // переход на фрагмент добавления квестов
            findNavController().navigate(R.id.nav_quests_user)
        }
    }

    private fun showWarningCardViewIfQuestIsEmpty() = with(binding) {
        if (viewModel.questUserRelated.value?.isEmpty() == true) {
            warnCardView.cardViewMessage.findViewById<TextView>(R.id.textWarningCardView)
                ?.text = getString(R.string.user_quests_is_empty)

            warnCardView.cardViewMessage.visibility = CardView.VISIBLE
        } else {
            warnCardView.cardViewMessage.visibility = CardView.GONE
        }
    }

    /**
     * Выводим модалку для подтверждения начала прохождения квеста
     */
    private fun onClickQuestUserItem(questUserRelated: QuestUserRelated) {
        val dialog = ChooseQuestDialogFragment(questUserRelated)

        dialog.show(parentFragmentManager, "dialog-choose-quest")
    }
}
