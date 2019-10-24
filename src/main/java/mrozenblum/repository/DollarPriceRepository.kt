package mrozenblum.repository

import mrozenblum.domain.response.dollarprice.DollarPriceResponse
import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.rest.Callback

interface DollarPriceRepository {
    fun dollarPrice(base: String, symbols: String, callback: Callback<DollarPriceResponse>)
}