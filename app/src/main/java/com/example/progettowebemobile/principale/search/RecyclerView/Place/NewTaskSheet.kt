package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.os.Bundle
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

        binding.minusButtonCategoria1.setOnClickListener {
            if (numberCamere > 0) {
                numberCamere--
                binding.numberTextViewCategoria1.text = numberCamere.toString()
            }
        }

        binding.plusButtonCategoria1.setOnClickListener {
            numberCamere++
            binding.numberTextViewCategoria1.text = numberCamere.toString()
        }
        binding.searchSheetSaveButton.setOnClickListener {
            saveAction()
        }

        binding.minusButtonCategoria2.setOnClickListener {
            if (numberAdulti > 0) {
                numberAdulti--
                binding.numberTextViewCategoria2.text = numberAdulti.toString()
            }
        }

        binding.plusButtonCategoria2.setOnClickListener {
            numberAdulti++
            binding.numberTextViewCategoria2.text = numberAdulti.toString()
        }
        binding.searchSheetSaveButton.setOnClickListener {
            saveAction()
        }

        binding.minusButtonCategoria3.setOnClickListener {
            if (numberBambini > 0) {
                numberBambini--
                binding.numberTextViewCategoria3.text = numberBambini.toString()
            }
        }

        binding.plusButtonCategoria3.setOnClickListener {
            numberBambini++
            binding.numberTextViewCategoria3.text = numberBambini.toString()
        }
        binding.searchSheetSaveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    private fun saveAction()
    {
        taskViewModel.categoria1.value = binding.numberTextViewCategoria1.text.toString()
        taskViewModel.categoria2.value = binding.numberTextViewCategoria2.text.toString()
        taskViewModel.categoria3.value = binding.numberTextViewCategoria3.text.toString()
        dismiss()
    }

}







