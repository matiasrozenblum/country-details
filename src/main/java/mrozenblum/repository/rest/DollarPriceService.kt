package mrozenblum.repository.rest

import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DollarPriceService {
    @GET("api/latest")
    fun getDollarPrice(@Query("access_key") accessKey: String,
                @Query("base") base: String,
                @Query("symbols") symbols: String): Call<DollarPriceResponse>
}