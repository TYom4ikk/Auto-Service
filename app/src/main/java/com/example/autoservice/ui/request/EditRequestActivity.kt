package com.example.autoservice.ui.request

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R
import com.example.autoservice.data.repository.RequestRepository
import com.example.autoservice.data.model.ServiceRequest
import java.text.SimpleDateFormat
import java.util.*

class EditRequestActivity : AppCompatActivity() {

    private lateinit var tvCarInfo: TextView
    private lateinit var tvServiceType: TextView
    private lateinit var etDate: EditText
    private lateinit var etDescription: EditText
    private lateinit var ivCalendar: ImageView

    private var currentRequest: ServiceRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_request)

        initViews()
        setupDatePicker()
        setupClickListeners()
        loadRequestData()
    }

    private fun initViews() {
        tvCarInfo = findViewById(R.id.tvCarInfo)
        tvServiceType = findViewById(R.id.tvServiceType)
        etDate = findViewById(R.id.etDate)
        etDescription = findViewById(R.id.etDescription)
        ivCalendar = findViewById(R.id.ivCalendar)
    }

    private fun setupDatePicker() {
        ivCalendar.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentDate = etDate.text.toString()
            
            if (currentDate.isNotEmpty()) {
                try {
                    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val date = sdf.parse(currentDate)
                    date?.let {
                        calendar.time = it
                    }
                } catch (e: Exception) {
                    // Оставляем текущую дату при ошибке парсинга
                }
            }

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

        findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
            showDeleteDialog()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveRequest()
        }
    }

    private fun loadRequestData() {
        val requestId = intent.getIntExtra("REQUEST_ID", -1)
        if (requestId == -1) {
            finish()
            return
        }

        currentRequest = RequestRepository.getRequests().find { it.id == requestId }
        currentRequest?.let { request ->
            tvCarInfo.text = "${request.carBrand} • ${request.carPlate}"
            tvServiceType.text = request.title
            etDate.setText(request.date)
            etDescription.setText(request.description)
        }
    }

    private fun saveRequest() {
        val date = etDate.text.toString()
        val description = etDescription.text.toString()

        if (date.isEmpty()) {
            Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Добавьте описание проблемы", Toast.LENGTH_SHORT).show()
            return
        }

        currentRequest?.let { request ->
            RequestRepository.updateRequest(
                id = request.id,
                title = request.title,
                status = request.status,
                carBrand = request.carBrand,
                carPlate = request.carPlate,
                description = description,
                date = date
            )

            Toast.makeText(this, "Заявка обновлена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Удалить заявку?")
            .setMessage("Вы уверены, что хотите удалить эту заявку? Это действие нельзя отменить.")
            .setPositiveButton("Удалить") { _, _ ->
                currentRequest?.let { request ->
                    RequestRepository.deleteRequest(request.id)
                    Toast.makeText(this, "Заявка удалена", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
