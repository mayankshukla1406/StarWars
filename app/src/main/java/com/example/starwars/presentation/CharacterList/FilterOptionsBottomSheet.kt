package com.example.starwars.presentation.CharacterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.starwars.R
import com.example.starwars.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentFilterBottomSheetBinding

    interface FilterOptionsListener {
        fun onFilterOptionsSelected(gender: String, eyeColor: String)
    }

    private var filterOptionsListener: FilterOptionsListener? = null

    fun setFilterOptionsListener(listener: FilterOptionsListener) {
        this.filterOptionsListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBottomSheetBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btDone.setOnClickListener {
            val selectedGender: String = when (binding.radioGroupName.checkedRadioButtonId) {
                R.id.MaleGender -> "Male"
                R.id.femaleGender -> "Female"
                else -> ""
            }
            val enteredEyeColor: String = binding.etEyeColor.text.toString()

            filterOptionsListener?.onFilterOptionsSelected(selectedGender, enteredEyeColor)
            dismiss()
        }
    }
}
