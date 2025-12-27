package com.example.autoservice.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R
import com.example.autoservice.data.repository.CarRepository
import com.example.autoservice.data.repository.RequestRepository
import com.example.autoservice.ui.request.CreateRequestActivity
import com.example.autoservice.ui.request.EditRequestActivity
import com.example.autoservice.ui.support.SupportChatActivity
import com.example.autoservice.ui.support.SupportChatActivitySimple
import com.example.autoservice.ui.support.SupportChatActivityFull

class MainScreenActivity : AppCompatActivity() {

    private lateinit var carsContainer: LinearLayout
    private lateinit var requestsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        carsContainer = findViewById(R.id.carsContainer)
        requestsContainer = findViewById(R.id.requestsContainer)

        findViewById<Button>(R.id.btnAddCar).setOnClickListener {
            showAddCarDialog()
        }

        findViewById<Button>(R.id.btnAddRequest).setOnClickListener {
            startActivity(Intent(this, CreateRequestActivity::class.java))
        }

        // Находим кнопку поддержки и добавляем обработчик
        val supportButton = findViewById<Button>(R.id.btnSupport)
        if (supportButton != null) {
            supportButton.setOnClickListener {
                Toast.makeText(this, "Открытие чата поддержки...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SupportChatActivityFull::class.java))
            }
        } else {
            Toast.makeText(this, "Кнопка поддержки не найдена", Toast.LENGTH_SHORT).show()
        }

        renderCars()
        renderRequests()
    }

    private fun renderCars() {
        carsContainer.removeAllViews()

        for (car in CarRepository.getCars()) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_car, carsContainer, false)

            view.findViewById<TextView>(R.id.tvCarBrand).text = car.brand
            view.findViewById<TextView>(R.id.tvCarInfo).text = "${car.year} • ${car.plate}"

            view.findViewById<ImageView>(R.id.ivDeleteCar).setOnClickListener {
                showDeleteDialog(car.id)
            }

            carsContainer.addView(view)
        }
    }

    private fun renderRequests() {
        requestsContainer.removeAllViews()

        for (request in RequestRepository.getRequests()) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_service_request, requestsContainer, false)

            view.findViewById<TextView>(R.id.tvRequestTitle).text = request.title
            view.findViewById<TextView>(R.id.tvRequestStatus).text = request.status
            view.findViewById<TextView>(R.id.tvRequestCar).text = "${request.carBrand} • ${request.carPlate}"
            view.findViewById<TextView>(R.id.tvRequestDescription).text = request.description
            view.findViewById<TextView>(R.id.tvRequestDate).text = request.date

            view.findViewById<ImageView>(R.id.ivEditRequest).setOnClickListener {
                val intent = Intent(this, EditRequestActivity::class.java)
                intent.putExtra("REQUEST_ID", request.id)
                startActivity(intent)
            }

            requestsContainer.addView(view)
        }
    }

    private fun showAddCarDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val brand = EditText(this).apply { hint = "Марка" }
        val year = EditText(this).apply {
            hint = "Год"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val plate = EditText(this).apply { hint = "Номер" }

        layout.addView(brand)
        layout.addView(year)
        layout.addView(plate)

        AlertDialog.Builder(this)
            .setTitle("Добавить автомобиль")
            .setView(layout)
            .setPositiveButton("Добавить") { _, _ ->
                CarRepository.addCar(
                    brand.text.toString(),
                    year.text.toString().toInt(),
                    plate.text.toString()
                )
                renderCars()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteDialog(carId: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_car, null)
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }
        
        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            CarRepository.deleteCar(carId)
            renderCars()
            dialog.dismiss()
        }
        
        dialog.show()
    }
}