package mrozenblum.domain

import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.domain.response.ipdata.DataResponse
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object StubFactory {

    fun createDefaultResponseForVenue() = DataResponse("", "", "", "")

    fun createSuccessFlightReservationsCallStub(countryDetails: CountryDetails): Call<CountryDetails> {
        return generateCallForType(countryDetails)
    }

    fun createSuccessVenuesCallStub(response: DataResponse): Call<DataResponse>? {
        return generateCallForType(response)
    }


    private inline fun <reified T: Any> generateCallForType(response: T) : Call<T> {
        return object: Call<T> {
            override fun enqueue(callback: Callback<T>) {
                callback.onResponse(this, Response.success(response))
            }

            override fun isExecuted() = true

            override fun clone() = this

            override fun isCanceled() = false

            override fun cancel() {}

            override fun execute() = Response.success(response)

            override fun request() = Request.Builder().build()
        }
    }


}