package com.example.autolife.model

data class EconomicIndicator(
    val serie: List<SerieData>
)

data class SerieData(
    val fecha: String,
    val valor: Double
)