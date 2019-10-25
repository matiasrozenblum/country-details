package mrozenblum

import com.sun.net.httpserver.HttpHandler
import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpServer
import mrozenblum.controller.DestinationController
import mrozenblum.controller.StatisticsController
import mrozenblum.repository.*
import mrozenblum.repository.rest.CountryDetailsService
import mrozenblum.repository.rest.DollarPriceService
import mrozenblum.repository.rest.GeoIPService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main(){
    val server = HttpServer.create(InetSocketAddress(9291), 0)
    val countryDetailsContext = server.createContext("/country/details")
    val statisticsContext = server.createContext("/statistics")

    countryDetailsContext.handler = HttpHandler { httpExchange ->
        DestinationController(
                RemoteGeoIPRepository(createGeoIPService()),
                RemoteCountryDetailsRepository(createCountryDetailsService()),
                RemoteDollarPriceRepository(createDollarPriceService()),
                InMemoryStatisticsRepository
        ).handle(httpExchange)
    }
    statisticsContext.handler = HttpHandler { httpExchange ->
        StatisticsController(
                InMemoryStatisticsRepository
        ).handle(httpExchange)
    }
    server.start()
}

private fun createCountryDetailsService()
        = createClient(RemoteCountryDetailsRepository.API_URL).create(CountryDetailsService::class.java)

private fun createGeoIPService() = createClient(RemoteGeoIPRepository.API_URL).create(GeoIPService::class.java)

private fun createDollarPriceService() = createClient(RemoteDollarPriceRepository.API_URL).create(DollarPriceService::class.java)

private fun createClient(url: String): Retrofit {
    return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
