package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NewTaskSheet(
    var id_luogo: Int,
    var nome1: String,
    var nome2: String,
    var nome3: String,
    var prezzo1: Int,
    var prezzo2: Int,
    var prezzo3: Int,
    var tipo: String,
    var place: PlaceFragment,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var numberCamere: Int = 0
    private var numberAdulti: Int = 0
    private var numberBambini: Int = 0
    private var prezzo = 0.0
    private var data1=""
    private var data2=""
    private var difGiorni=1
    private var utils= Utils()
    private var listaPrezzi = arrayOf(prezzo1, prezzo2, prezzo3)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        if (tipo == "ristorante") {
            binding.serchFragmentTvPrezzo.text = getString(R.string.Place_TV_PrezzoGenerale_Ristorante)
        }else{
            binding.serchFragmentTvPrezzo.text = getString(R.string.Place_TV_Prezzo_Al_Giorno)
        }

        binding.sheetData.setOnClickListener {
            if (tipo == "hotel") {

                showDateRangePicker()
            } else {
                showDatePicker()
            }
        }

        binding.minusButtonCategoria1.setOnClickListener {
            if (numberCamere > 0) {
                numberCamere--
                prezzo -= listaPrezzi[0]
                binding.prezzo.text = prezzo.toString()
                binding.numberTextViewCategoria1.text = numberCamere.toString()
            }
        }

        binding.plusButtonCategoria1.setOnClickListener {
            numberCamere++
            prezzo += listaPrezzi[0]
            binding.prezzo.text = prezzo.toString()
            binding.numberTextViewCategoria1.text = numberCamere.toString()
        }

        binding.minusButtonCategoria2.setOnClickListener {
            if (numberAdulti > 0) {
                numberAdulti--
                prezzo -= listaPrezzi[1]
                binding.prezzo.text = prezzo.toString()
                binding.numberTextViewCategoria2.text = numberAdulti.toString()
            }
        }

        binding.plusButtonCategoria2.setOnClickListener {
            numberAdulti++
            prezzo += listaPrezzi[1]
            binding.prezzo.text = prezzo.toString()
            binding.numberTextViewCategoria2.text = numberAdulti.toString()
        }

        binding.minusButtonCategoria3.setOnClickListener {
            if (numberBambini > 0) {
                prezzo -= listaPrezzi[2]
                numberBambini--
                binding.prezzo.text = prezzo.toString()
                binding.numberTextViewCategoria3.text = numberBambini.toString()
            }
        }

        binding.plusButtonCategoria3.setOnClickListener {
            numberBambini++
            prezzo += listaPrezzi[2]
            binding.prezzo.text = prezzo.toString()
            binding.numberTextViewCategoria3.text = numberBambini.toString()
        }

        binding.searchSheetSaveButton.setOnClickListener {
            if(data1 == ""){
                utils.PopError(getString(R.string.Place_pagamento_dataError_title),getString(R.string.Place_pagamento_dataError_text),requireContext())
            }else {
                if (prezzo==0.0) {
                    utils.PopError(getString(R.string.Place_pagamento_prezzoError_title),getString(R.string.Place_pagamento_prezzoError_text),requireContext())
                }else{
                    if (tipo == "hotel") {
                        place.prezzo = prezzo * (difGiorni + 1)
                    } else {
                        place.prezzo = prezzo * difGiorni
                    }
                    place.dataPrenotazione1 = data1
                    place.dataPrenotazione2 = data2
                    place.loadRecyclerViewData()
                    saveAction()
                }
            }
        }

        binding.searchSheetCategoria1.text = nome1
        binding.searchSheetCategoria2.text = nome2
        binding.searchSheetCategoria3.text = nome3
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        taskViewModel.categoria1.value = binding.numberTextViewCategoria1.text.toString()
        taskViewModel.categoria2.value = binding.numberTextViewCategoria2.text.toString()
        taskViewModel.categoria3.value = binding.numberTextViewCategoria3.text.toString()
        dismiss()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
            data1=formattedDate
            binding.sheetData.text = formattedDate
        }, year, month, day)

        // Imposta il limite inferiore per il selettore di date come la data odierna
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }


    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val startDate = Calendar.getInstance()
            startDate.set(selectedYear, selectedMonth, selectedDay)

            val endDatePickerDialog = DatePickerDialog(requireContext(), { _, endYear, endMonth, endDay ->
                val endDate = Calendar.getInstance()
                endDate.set(endYear, endMonth, endDay)

                // Calcola la differenza di giorni tra startDate e endDate
                val diffInMillis = endDate.timeInMillis - startDate.timeInMillis
                val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)

                difGiorni = diffInDays.toInt()


                val startDateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startDate.time)
                val endDateFormatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate.time)

                data1 = startDateFormatted
                data2 = endDateFormatted

                binding.sheetData.text = getString(R.string.date_range_format, startDateFormatted, endDateFormatted)

            }, year, month, day)

            // Imposta il limite inferiore per il selettore di date di fine come la data di inizio
            endDatePickerDialog.datePicker.minDate = startDate.timeInMillis

            endDatePickerDialog.show()
        }, year, month, day)

        // Imposta il limite inferiore per il selettore di date di inizio come la data odierna
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

}








