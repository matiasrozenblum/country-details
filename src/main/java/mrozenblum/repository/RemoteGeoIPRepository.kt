package mrozenblum.repository

import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.rest.Callback
import mrozenblum.repository.rest.GeoIPService
import retrofit2.Call
import retrofit2.Response

class RemoteGeoIPRepository(
        private val service: GeoIPService
): GeoIPRepository {
    override fun data(ip: String, callback: Callback<DataResponse?>) {
        service.getData(ip).enqueue(object: retrofit2.Callback<DataResponse>{
            override fun onFailure(call: Call<DataResponse>, error: Throwable) {
                callback.onError(error)
            }

            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                callback.onComplete(response.body())
            }
        })
    }

     companion object{
         const val API_URL = "https://api.ip2country.info/"
    }
}