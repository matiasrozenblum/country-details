package mrozenblum.controller

import kotlin.math.acos
import com.google.gson.GsonBuilder
import com.sun.net.httpserver.HttpExchange
import mrozenblum.domain.exception.ErrorGettingResponseException
import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.domain.response.Response
import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.CountryDetailsRepository
import mrozenblum.repository.DollarPriceRepository
import mrozenblum.repository.GeoIPRepository
import mrozenblum.repository.StatisticsRepository
import mrozenblum.repository.rest.Callback
import java.util.*
import java.util.TimeZone
import java.text.SimpleDateFormat
import kotlin.math.cos
import kotlin.math.sin

class DestinationController(
        private val geoIPRepository: GeoIPRepository,
        private val countryDetailsRepository: CountryDetailsRepository,
        private val dollarPriceRepository: DollarPriceRepository,
        private val statisticsRepository: StatisticsRepository
) {
    private var response: Response = Response()
    private var ip: String = ""
    private lateinit var exchange: HttpExchange
    private var bsAsLatLng : DoubleArray = doubleArrayOf(-34.6, -58.38)

    fun handle(exchange: HttpExchange){
        this.exchange = exchange
        prepareParams()
        try {
            getIpData(ip)
        } catch (e: Exception) {
            error(exchange, e)
        }
    }

    private fun prepareParams() {
        ip = ParamParser.getParamFromUri(exchange.requestURI.query, "ip")
    }

    private fun getIpData(ip: String) {
        geoIPRepository.data(ip, object : Callback<DataResponse?> {
            override fun onComplete(value: DataResponse?) {
                if(value?.countryCode3 == null) throw Exception()
                response = response.copy(country = value.countryName, isoCode = value.countryCode3)
                getCountryDetails(value.countryCode3)
            }

            override fun onError(error: Throwable) {
                throw ErrorGettingResponseException(error)
            }

        })
    }

    private fun getCountryDetails(code: String) {
        countryDetailsRepository.details(code, object : Callback<CountryDetails> {
            override fun onComplete(value: CountryDetails) {
                val times = times(value.timezones)
                val distance = distance(bsAsLatLng[0], bsAsLatLng[1], value.latlng[0].toDouble(), value.latlng[1].toDouble())
                statisticsRepository.addDistance(distance)
                response = response.copy(language = value.languages.map { it.name },dateTimes = times ,currency = value.currencies[0].code, distance = "${"%.2f".format(distance)} km")
                getDollarRate(value.currencies[0].code)
            }

            override fun onError(error: Throwable) {
                throw ErrorGettingResponseException(error)
            }
        })
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        if (lat1 == lat2 && lon1 == lon2) {
            return 0.0
        } else {
            val theta = lon1 - lon2
            var dist = sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * cos(Math.toRadians(theta))
            dist = acos(dist)
            dist = Math.toDegrees(dist)
            dist *= 60.0 * 1.1515 * 1.609344
            return dist
        }
    }


    private fun times(time: List<String>): List<String> {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val timeList = mutableListOf<String>()
        time.forEach{
            dateFormat.timeZone = TimeZone.getTimeZone(it.replace("UTC", "GMT"))
            timeList.add("${dateFormat.format(Date())} ($it)")
        }
        return timeList
    }

    private fun getDollarRate(code: String) {
        dollarPriceRepository.dollarPrice(code, "USD", object : Callback<DollarPriceResponse> {
            override fun onComplete(value: DollarPriceResponse) {
                var rate :Float? = 1.toFloat()
                if(code != "USD")
                    rate = value.rates["USD"]
                response = response.copy(currency = "$code (1 $code = $rate USD")
                updateResponseBody()
            }

            override fun onError(error: Throwable) {
                throw ErrorGettingResponseException(error)
            }
        })
    }

    private fun updateResponseBody(){
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        val responseByteArray = gson.toJson(response).toByteArray()
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