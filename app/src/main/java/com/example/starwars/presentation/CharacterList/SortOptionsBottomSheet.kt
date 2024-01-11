package com.example.starwars.presentation.CharacterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.starwars.databinding.FragmentSortOptionsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentSortOptionsBottomSheetBinding

    private var sortOptionsListener: SortOptionsListener? = null


    fun setListener(listener: SortOptionsListener) {
        this.sortOptionsListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSortOptionsBottomSheetBinding.inflate(layoutInflater,container,false)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRadioGroupListeners()
    }

    private fun setupRadioGroupListeners() {
        setupRadioGroupListener(binding.radioGroupName, binding.ascendingButtonName, binding.descendingButtonName, SortAttribute.NAME)
        setupRadioGroupListener(binding.radioGroupHeight, binding.ascendingButtonHeight, binding.descendingButtonHeight, SortAttribute.HEIGHT)
        setupRadioGroupListener(binding.radioGroupMass, binding.ascendingButtonMass, binding.descendingButtonMass, SortAttribute.MASS)
    }

    private fun setupRadioGroupListener(
        radioGroup: RadioGroup,
        ascendingRadioButton: RadioButton,
        descendingRadioButton: RadioButton,
        sortAttribute: SortAttribute
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                ascendingRadioButton.id -> handleSortingOptionSelected(sortAttribute, SortOrder.ASCENDING)
                descendingRadioButton.id -> handleSortingOptionSelected(sortAttribute, SortOrder.DESCENDING)
            }
        }
    }

    private fun handleSortingOptionSelected(sortAttribute: SortAttribute, sortOrder: SortOrder) {
        sortOptionsListener?.onSortOptionSelected(sortAttribute, sortOrder)
        dismiss()
    }
}
