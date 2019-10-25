package mrozenblum.controller

import com.sun.net.httpserver.HttpExchange
import mrozenblum.domain.response.StatisticsResponse
import mrozenblum.repository.StatisticsRepository
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.InputStream
import java.io.OutputStream
import java.net.URI

class StatisticsControllerTest {
    private val statisticsRepository = Mockito.mock(StatisticsRepository::class.java)
    private val statisticsController = StatisticsController(statisticsRepository)

    @Test
    fun `when new request then call statistics repository`() {
        val uri = URI("http", null,  "localhost", 9291, "/statistics", "", "")
        val exchange = Mockito.mock(HttpExchange::class.java)
        Mockito.`when`(exchange.requestURI).thenReturn(uri)
        Mockito.`when`(exchange.requestBody).thenReturn(Mockito.mock(InputStream::class.java))
        Mockito.`when`(exchange.responseBody).thenReturn(Mockito.mock(OutputStream::class.java))
        statisticsController.handle(exchange)
        Mockito.verify(statisticsRepository).greatestDistance()
        Mockito.verify(statisticsRepository).closestDistance()
        Mockito.verify(statisticsRepository).averageDistance()
    }
}