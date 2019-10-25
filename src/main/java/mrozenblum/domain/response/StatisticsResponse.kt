package mrozenblum.domain.response

data class StatisticsResponse(val greatestDistance: String? = null,
                              val closestDistance: String? = null,
                              val averageDistance: String? = null)