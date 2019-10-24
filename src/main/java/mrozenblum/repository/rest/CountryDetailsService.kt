package mrozenblum.repository.rest

import mrozenblum.domain.response.countrydetails.CountryDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryDetailsService {
    @GET("rest/v2/alpha/{code}")
    fun getDetails(@Path("code")code: String): Call<CountryDetails>
}