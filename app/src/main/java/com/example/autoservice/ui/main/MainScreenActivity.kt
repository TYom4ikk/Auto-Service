package com.example.autoservice.ui.main

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.autoservice.R
import com.example.autoservice.data.repository.CarRepository

class MainScreenActivity : AppCompatActivity() {

    private lateinit var carsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        carsContainer = findViewById(R.id.carsContainer)

        findViewById<Button>(R.id.btnAddCar).setOnClickListener {
            showAddCarDialog()
        }

        renderCars()
    }

    private fun renderCars() {
        carsContainer.removeAllViews()

        for (car in CarRepository.getCars()) {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, carsContainer, false)

            view.findViewById<TextView>(android.R.id.text1).text =
                car.brand

            view.findViewById<TextView>(android.R.id.text2).text =
                "${car.year} • ${car.plate}"

            view.setOnLongClickListener {
                showDeleteDialog(car.id)
                true
            }

            carsContainer.addView(view)
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
        AlertDialog.Builder(this)
            .setTitle("Удалить автомобиль?")
            .setMessage("Это действие нельзя отменить")
            .setPositiveButton("Удалить") { _, _ ->
                CarRepository.deleteCar(carId)
                renderCars()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}