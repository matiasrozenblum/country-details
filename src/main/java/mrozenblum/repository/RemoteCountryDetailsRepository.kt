package mrozenblum.repository

import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.repository.rest.Callback
import mrozenblum.repository.rest.CountryDetailsService
import retrofit2.Call
import retrofit2.Response


class RemoteCountryDetailsRepository(
        private val serviceFlight: CountryDetailsService
): CountryDetailsRepository {

    override fun details(code: String, callback: Callback<CountryDetails>) {
        serviceFlight.getDetails(code).enqueue(object: retrofit2.Callback<CountryDetails>{
            override fun onFailure(call: Call<CountryDetails>, error: Throwable) {
                callback.onError(error)
            }

            override fun onResponse(call: Call<CountryDetails>, response: Response<CountryDetails>) {
                callback.onComplete(response.body()?: CountryDetails(listOf(), listOf(), listOf(), listOf()))
            }
        })
    }

     companion object{
        const val API_URL = "https://restcountries.eu/"
    }
}