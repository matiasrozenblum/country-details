package mrozenblum.repository.rest

import mrozenblum.domain.response.ipdata.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryName

interface GeoIPService {
    @GET("/ip")
    fun getData(@QueryName ip: String): Call<DataResponse>
}