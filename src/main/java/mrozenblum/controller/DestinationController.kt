package mrozenblum.controller

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.sun.net.httpserver.HttpExchange
import mrozenblum.domain.exception.ErrorGettingResponseException
import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.domain.response.Response
import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.CountryDetailsRepository
import mrozenblum.repository.DollarPriceRepository
import mrozenblum.repository.GeoIPRepository
import mrozenblum.repository.rest.Callback
import java.util.*
import java.util.TimeZone
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.text.html.parser.Parser
import kotlin.collections.ArrayList


class DestinationController(
        private val geoIPRepository: GeoIPRepository,
        private val countryDetailsRepository: CountryDetailsRepository,
        private val dollarPriceRepository: DollarPriceRepository
) {
    private var response: Response = Response()
    private var ip: String = ""
    private var code: String = ""
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
                val times = getTimes(value.timezones)
                val distance = Math.sqrt(Math.pow(bsAsLatLng[0] - value.latlng[0], 2.0) + Math.pow(bsAsLatLng[1] - value.latlng[1], 2.0))
                response = response.copy(language = value.languages.map { it.name },dateTimes = times ,currency = value.currencies[0].code, distance = "${"%.2f".format(distance)} km")
                getDollarRate(value.currencies[0].code)
            }

            override fun onError(error: Throwable) {
                throw ErrorGettingResponseException(error)
            }
        })
    }

    private fun getTimes(time: List<String>): List<String> {
        val df = SimpleDateFormat("HH:mm:ss")
        val timeList = mutableListOf<String>()
        time.forEach{
            df.timeZone = TimeZone.getTimeZone(it.replace("UTC", "GMT"))
            timeList.add("${df.format(Date())} ($it)")
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