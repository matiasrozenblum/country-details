package mrozenblum.repository

import mrozenblum.domain.StubFactory
import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.domain.response.countrydetails.Currency
import mrozenblum.domain.response.countrydetails.Language
import mrozenblum.kotlinAny
import mrozenblum.repository.rest.Callback
import mrozenblum.repository.rest.CountryDetailsService
import mrozenblum.repository.rest.GeoIPService
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call

class RemoteCountryDetailsRepositoryTest {

    private val countryDetailsService = Mockito.mock(CountryDetailsService::class.java)
    private val repository = RemoteCountryDetailsRepository(countryDetailsService)

    private val countryDetails = CountryDetails(listOf(1.0.toFloat()), listOf("UTC"), listOf(Currency("USD")), listOf(Language("English")))

    @Test
    fun `when get reservations success then return reservations`() {
        Mockito.`when`(countryDetailsService.getDetails(kotlinAny()))
                .thenReturn(StubFactory.createSuccessFlightReservationsCallStub(countryDetails))

        repository.details("", object : Callback<CountryDetails> {
            override fun onComplete(value: CountryDetails) {
                assertEquals(countryDetails, value)
            }

            override fun onError(error: Throwable) {
            }
        })
    }


}