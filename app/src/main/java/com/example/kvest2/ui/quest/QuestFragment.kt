package com.example.kvest2.ui.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.databinding.QuestFragmentBinding
import com.example.kvest2.ui.quest.dialog.ChooseQuestDialogFragment


class QuestFragment : Fragment() {
    companion object {
        fun newInstance() = QuestFragment()
    }

    private lateinit var viewModel: QuestViewModel
    private lateinit var binding: QuestFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuestFragmentBinding.inflate(inflater, container, false)
        val currentUser = AppUserSingleton.getUser()

        viewModel = ViewModelProvider(this, QuestViewModelFactory(binding.root.context))
            .get(QuestViewModel::class.java)

        binding.apply {
            questRecycleView.layoutManager = LinearLayoutManager(context)

            viewModel.quests.observe(viewLifecycleOwner) {
                questRecycleView.adapter = QuestAdapter(it) { quest ->
                    ChooseQuestDialogFragment(quest) {
                        viewModel.setChosenQuest(quest, currentUser!!)
                    }.show(parentFragmentManager, "dialog-choose-quest")
                }
            }

            testBtnForAddQuest.setOnClickListener {
                viewModel.add(Quest(0, "sd", "df", "fds"))
            }
        }

        return binding.root
    }
}
