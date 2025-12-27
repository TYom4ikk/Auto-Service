package com.example.autoservice.data.repository

import com.example.autoservice.data.model.ServiceRequest

object RequestRepository {

    private val requests = mutableListOf<ServiceRequest>(
        ServiceRequest(
            id = 1,
            title = "Техническая диагностика",
            status = "В работе",
            carBrand = "Toyota Camry",
            carPlate = "A123BC777",
            description = "Полная диагностика двигателя и ходовой части",
            date = "15.10.2025"
        )
    )

    private var idCounter = 1

    fun getRequests(): List<ServiceRequest> = requests

    fun addRequest(
        title: String,
        status: String,
        carBrand: String,
        carPlate: String,
        description: String,
        date: String
    ) {
        requests.add(
            ServiceRequest(
                id = ++idCounter,
                title = title,
                status = status,
                carBrand = carBrand,
                carPlate = carPlate,
                description = description,
                date = date
            )
        )
    }

    fun deleteRequest(id: Int) {
        requests.removeIf { it.id == id }
    }

    fun updateRequest(
        id: Int,
        title: String,
        status: String,
        carBrand: String,
        carPlate: String,
        description: String,
        date: String
    ) {
        val index = requests.indexOfFirst { it.id == id }
        if (index != -1) {
            requests[index] = ServiceRequest(
                id = id,
                title = title,
                status = status,
                carBrand = carBrand,
                carPlate = carPlate,
                description = description,
                date = date
            )
        }
    }
}