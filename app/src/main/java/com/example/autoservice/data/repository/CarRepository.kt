package com.example.autoservice.data.repository

import com.example.autoservice.data.model.Car

object CarRepository {

    private val cars = mutableListOf<Car>(
        Car(1, "Toyota Camry", 2020, "A123BC777"),
        Car(2, "BMW X5", 2021, "B456EK199")
    )

    private var idCounter = 2

    fun getCars(): List<Car> = cars

    fun addCar(brand: String, year: Int, plate: String) {
        cars.add(
            Car(++idCounter, brand, year, plate)
        )
    }

    fun deleteCar(id: Int) {
        cars.removeIf { it.id == id }
    }
}
