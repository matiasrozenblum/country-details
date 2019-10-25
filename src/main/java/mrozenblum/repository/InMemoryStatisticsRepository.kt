package mrozenblum.repository

object InMemoryStatisticsRepository: StatisticsRepository {
    private var greatestDistance: Double? = null
    private var closestDistance: Double? = null
    private var averageDistance: Double? = null
    private val callsCount: MutableMap<Double, Int> = mutableMapOf()
    override fun greatestDistance() = greatestDistance

    override fun closestDistance() = closestDistance

    override fun averageDistance(): Double? {
        var totalDistance = 0.0
        callsCount.forEach{entry ->
            totalDistance += entry.key * entry.value
        }
        return totalDistance / callsCount.values.sum()
    }

    override fun addDistance(distance: Double) {
        if(callsCount[distance] == null){
            callsCount[distance] = 0
        }
        callsCount[distance] = callsCount[distance]!!.plus(1)

        if(greatestDistance == null || greatestDistance!! < distance){
            greatestDistance = distance
        }
        if(closestDistance == null || closestDistance!! > distance){
            closestDistance = distance
        }

    }

}
