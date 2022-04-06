package com.example.kvest2.ui.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvest2.R
import com.example.kvest2.data.entity.QuestUser
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

        binding.apply {
            questsUserRecycleView.layoutManager = LinearLayoutManager(context)

            // отслеживаем список пользовательских квестов
            viewModel.questsUser.observe(viewLifecycleOwner) {
                questsUserRecycleView.adapter = QuestUserAdapter(it) { questUser ->
                    onClickQuestUserItem(questUser)
                }
            }

            // переход на фрагмент добавления квестов
            testBtnForAddQuest.setOnClickListener {
                findNavController().navigate(R.id.nav_quests_user)
            }
        }

        return binding.root
    }

    /**
     * Выводим модалку для подтверждения начала прохождения квеста
     */
    private fun onClickQuestUserItem(questUser: QuestUser) {
        val dialog = ChooseQuestDialogFragment(questUser) {
            viewModel.setChosenQuestUserHowCurrent(questUser)
        }

        dialog.show(parentFragmentManager, "dialog-choose-quest")
    }
}
