package com.example.autoservice.ui.request

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R
import com.example.autoservice.data.repository.CarRepository
import com.example.autoservice.data.repository.RequestRepository
import java.text.SimpleDateFormat
import java.util.*

class CreateRequestActivity : AppCompatActivity() {

    private lateinit var spinnerCar: Spinner
    private lateinit var spinnerServiceType: Spinner
    private lateinit var etDate: EditText
    private lateinit var etDescription: EditText
    private lateinit var ivCalendar: ImageView

    private val serviceTypes = arrayOf(
        "Выберите тип услуги",
        "Техническая диагностика",
        "Замена масла",
        "Ремонт тормозов",
        "Ремонт двигателя",
        "Шиномонтаж",
        "Диагностика ходовой части",
        "Ремонт электрики",
        "Кузовной ремонт",
        "Другое"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_request)

        initViews()
        setupSpinners()
        setupDatePicker()
        setupClickListeners()
    }

    private fun initViews() {
        spinnerCar = findViewById(R.id.spinnerCar)
        spinnerServiceType = findViewById(R.id.spinnerServiceType)
        etDate = findViewById(R.id.etDate)
        etDescription = findViewById(R.id.etDescription)
        ivCalendar = findViewById(R.id.ivCalendar)
    }

    private fun setupSpinners() {
        // Настройка спиннера для автомобилей
        val cars = CarRepository.getCars()
        val carOptions = mutableListOf("Выберите автомобиль")
        cars.forEach { car ->
            carOptions.add("${car.brand} ${car.year} • ${car.plate}")
        }

        val carAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, carOptions)
        carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCar.adapter = carAdapter

        // Настройка спиннера для типов услуг
        val serviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceTypes)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceType.adapter = serviceAdapter
    }

    private fun setupDatePicker() {
        ivCalendar.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                    etDate.setText(formattedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnCreate).setOnClickListener {
            createRequest()
        }
    }

    private fun createRequest() {
        val selectedCarPosition = spinnerCar.selectedItemPosition
        val selectedServicePosition = spinnerServiceType.selectedItemPosition
        val date = etDate.text.toString()
        val description = etDescription.text.toString()

        // Валидация
        if (selectedCarPosition == 0) {
            Toast.makeText(this, "Выберите автомобиль", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedServicePosition == 0) {
            Toast.makeText(this, "Выберите тип услуги", Toast.LENGTH_SHORT).show()
            return
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Добавьте описание проблемы", Toast.LENGTH_SHORT).show()
            return
        }

        // Получаем данные об автомобиле
        val cars = CarRepository.getCars()
        val selectedCar = cars[selectedCarPosition - 1]

        // Создаем заявку
        RequestRepository.addRequest(
            title = serviceTypes[selectedServicePosition],
            status = "В работе",
            carBrand = selectedCar.brand,
            carPlate = selectedCar.plate,
            description = description,
            date = date
        )

        Toast.makeText(this, "Заявка создана успешно", Toast.LENGTH_SHORT).show()
        finish()
    }
}
