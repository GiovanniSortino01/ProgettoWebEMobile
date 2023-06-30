package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewTaskSheet(var id_luogo:Int,var nome1:String,var nome2:String,var nome3:String,var prezzo1:Int,var prezzo2:Int,var prezzo3:Int,var tipo:String) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var numberCamere: Int = 0
    private var numberAdulti: Int = 0
    private var numberBambini: Int = 0
    private var prezzo = 0
    private var item = Buffer()
    private var listaPrezzi = arrayOf(prezzo1, prezzo2, prezzo3)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        if(tipo == "ristorante"){
            binding.prezzo.visibility = View.GONE
        }

        binding.minusButtonCategoria1.setOnClickListener {
            if (numberCamere > 0) {
                numberCamere--
                prezzo = prezzo - listaPrezzi[0]
                binding.prezzo.text=prezzo.toString()
                binding.numberTextViewCategoria1.text = numberCamere.toString()
            }
        }

        binding.plusButtonCategoria1.setOnClickListener {
            numberCamere++
            prezzo = prezzo + listaPrezzi[0]
            binding.prezzo.text=prezzo.toString()
            binding.numberTextViewCategoria1.text = numberCamere.toString()
        }

        binding.minusButtonCategoria2.setOnClickListener {
            if (numberAdulti > 0) {
                numberAdulti--
                prezzo = prezzo - listaPrezzi[1]
                binding.prezzo.text=prezzo.toString()
                binding.numberTextViewCategoria2.text = numberAdulti.toString()
            }
        }

        binding.plusButtonCategoria2.setOnClickListener {
            numberAdulti++
            prezzo = prezzo + listaPrezzi[1]
            binding.prezzo.text=prezzo.toString()
            binding.numberTextViewCategoria2.text = numberAdulti.toString()

        }

        binding.minusButtonCategoria3.setOnClickListener {
            if (numberBambini > 0) {
                prezzo = prezzo - listaPrezzi[2]
                numberBambini--
                binding.prezzo.text=prezzo.toString()
                binding.numberTextViewCategoria3.text = numberBambini.toString()
            }
        }

        binding.plusButtonCategoria3.setOnClickListener {
            numberBambini++
            prezzo = prezzo + listaPrezzi[2]
            binding.prezzo.text=prezzo.toString()
            binding.numberTextViewCategoria3.text = numberBambini.toString()
        }


        binding.searchSheetSaveButton.setOnClickListener {
            saveAction()
        }

        binding.searchSheetCategoria1.text=nome1
        binding.searchSheetCategoria2.text=nome2
        binding.searchSheetCategoria3.text=nome3

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







