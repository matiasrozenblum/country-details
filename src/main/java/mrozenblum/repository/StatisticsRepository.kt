package mrozenblum.repository

interface StatisticsRepository {
    fun greatestDistance(): Double?
    fun closestDistance(): Double?
    fun averageDistance(): Double?
    fun addDistance(distance: Double)
}