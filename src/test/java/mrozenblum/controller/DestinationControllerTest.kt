package mrozenblum.controller

import com.sun.net.httpserver.HttpExchange
import mrozenblum.kotlinAny
import mrozenblum.repository.CountryDetailsRepository
import mrozenblum.repository.DollarPriceRepository
import mrozenblum.repository.GeoIPRepository
import mrozenblum.repository.StatisticsRepository
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.InputStream
import java.net.URI

class DestinationControllerTest{
    private val geoIPRepository = Mockito.mock(GeoIPRepository::class.java)
    private val countryDetailsRepository = Mockito.mock(CountryDetailsRepository::class.java)
    private val dollarPriceRepository = Mockito.mock(DollarPriceRepository::class.java)
    private val statisticsRepository = Mockito.mock(StatisticsRepository::class.java)
    private val destinationController = DestinationController(geoIPRepository, countryDetailsRepository, dollarPriceRepository, statisticsRepository)

    @Test
    fun `when new request then call both repositories`() {
        val uri = URI("http", null,  "localhost", 9291, "/country/details", "ip=5.6.7.8", null)
        val exchange = Mockito.mock(HttpExchange::class.java)
        Mockito.`when`(exchange.requestURI).thenReturn(uri)
        Mockito.`when`(exchange.requestBody).thenReturn(Mockito.mock(InputStream::class.java))
        destinationController.handle(exchange)
        Mockito.verify(geoIPRepository).data(kotlinAny(), kotlinAny())
    }
}