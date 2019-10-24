package mrozenblum.repository

import mrozenblum.domain.response.ipdata.DataResponse
import mrozenblum.repository.rest.Callback

interface GeoIPRepository {
    fun data(ip: String, callback: Callback<DataResponse?>)
}