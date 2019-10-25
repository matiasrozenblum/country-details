package mrozenblum.controller

import kotlin.math.acos
import com.google.gson.GsonBuilder
import com.sun.net.httpserver.HttpExchange
import mrozenblum.domain.exception.ErrorGettingResponseException
import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.domain.response.Response
import mrozenblum.domain.response.StatisticsResponse
import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.InMemoryStatisticsRepository
import mrozenblum.repository.StatisticsRepository
import mrozenblum.repository.rest.Callback
import java.util.*
import java.util.TimeZone
import java.text.SimpleDateFormat
import kotlin.math.cos
import kotlin.math.sin

class StatisticsController(
        private val statisticsRepository: StatisticsRepository
) {
    private lateinit var exchange: HttpExchange

    fun handle(exchange: HttpExchange){
        this.exchange = exchange
        try {
            val response = createResponse()
            updateResponseBody(response)
        } catch (e: Exception) {
            error(exchange, e)
        }
    }

    private fun createResponse(): StatisticsResponse{
        return StatisticsResponse(
                greatestDistance = "${"%.2f".format(statisticsRepository.greatestDistance())} km",
                closestDistance = "${"%.2f".format(statisticsRepository.closestDistance())} km",
                averageDistance = "${"%.2f".format(statisticsRepository.averageDistance())} km")
    }

    private fun updateResponseBody(statisticsResponse: StatisticsResponse){
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        val responseByteArray = gson.toJson(statisticsResponse).toByteArray()
        exchange.sendResponseHeaders(SERVER_STATUS_OK, responseByteArray.size.toLong())
        val os = exchange.responseBody
        os.write(responseByteArray)
        os.close()
    }

    private fun error(exchange: HttpExchange, throwable: Throwable){
        print("Exception error on request:  ${throwable.message}")
        exchange.sendResponseHeaders(SERVER_STATUS_ERROR, 0)
        exchange.responseBody.close()
    }
    private companion object{
        const val SERVER_STATUS_OK = 200
        const val SERVER_STATUS_ERROR = 400
    }
}