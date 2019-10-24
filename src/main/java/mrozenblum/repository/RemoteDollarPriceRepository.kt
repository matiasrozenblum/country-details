package mrozenblum.repository

import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.rest.Callback
import mrozenblum.repository.rest.DollarPriceService
import retrofit2.Call
import retrofit2.Response

class RemoteDollarPriceRepository(
        private val service: DollarPriceService
): DollarPriceRepository {
    override fun dollarPrice(base: String, symbols: String, callback: Callback<DollarPriceResponse>) {
        service.getDollarPrice(API_KEY, base, symbols).enqueue(object: retrofit2.Callback<DollarPriceResponse>{
            override fun onFailure(call: Call<DollarPriceResponse>, error: Throwable) {
                callback.onError(error)
            }

            override fun onResponse(call: Call<DollarPriceResponse>, response: Response<DollarPriceResponse>) {
                callback.onComplete(response.body() ?: DollarPriceResponse(mapOf()))
            }
        })
    }

    companion object{
        const val API_URL = "http://data.fixer.io/"
        const val API_KEY = "4c3226a1ea34c734d2e1daedde065142"
    }
}