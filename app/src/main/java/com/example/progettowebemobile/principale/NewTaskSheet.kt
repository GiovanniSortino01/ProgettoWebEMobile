package com.example.progettowebemobile.principale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.progettowebemobile.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskSheet : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var numberCamere: Int = 0
    private var numberAdulti: Int = 0
    private var numberBambini: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        binding.minusButtonCamere.setOnClickListener {
            if (numberCamere > 0) {
                numberCamere--
                binding.numberTextViewCamere.text = numberCamere.toString()
            }
        }

        binding.plusButtonCamere.setOnClickListener {
            numberCamere++
            numberAdulti++
            binding.numberTextViewCamere.text = numberCamere.toString()
            binding.numberTextViewAdulti.text = numberAdulti.toString()
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.minusButtonAdulti.setOnClickListener {
            if (numberAdulti > 0) {
                numberAdulti--
                binding.numberTextViewAdulti.text = numberAdulti.toString()
            }
        }

        binding.plusButtonAdulti.setOnClickListener {
            numberAdulti++
            binding.numberTextViewAdulti.text = numberAdulti.toString()
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.minusButtonBambini.setOnClickListener {
            if (numberBambini > 0) {
                numberBambini--
                binding.numberTextViewBambini.text = numberBambini.toString()
            }
        }

        binding.plusButtonBambini.setOnClickListener {
            numberBambini++
            binding.numberTextViewBambini.text = numberBambini.toString()
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    private fun saveAction()
    {
        taskViewModel.camere.value = binding.numberTextViewCamere.text.toString()
        taskViewModel.adulti.value = binding.numberTextViewAdulti.text.toString()
        taskViewModel.bambini.value = binding.numberTextViewBambini.text.toString()
        dismiss()
    }

}







