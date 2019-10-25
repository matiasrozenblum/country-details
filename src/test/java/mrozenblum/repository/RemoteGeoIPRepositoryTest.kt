package mrozenblum.repository

import mrozenblum.domain.StubFactory
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.rest.Callback
import mrozenblum.repository.rest.GeoIPService
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RemoteGeoIPRepositoryTest {
    private val geoIpService = Mockito.mock(GeoIPService::class.java)
    private val repository = RemoteGeoIPRepository(geoIpService)

    @Test
    fun `when get venues success then return venues`() {
        val response = StubFactory.createDefaultResponseForVenue()

        Mockito.`when`(geoIpService.getData("5.6.7.8"))
                .thenReturn(StubFactory.createSuccessVenuesCallStub(response))

        repository.data("5.6.7.8", object : Callback<DataResponse?> {
            override fun onComplete(value: DataResponse?) {
                assertEquals(response, value)
            }

            override fun onError(error: Throwable) {
            }
        })
    }
}