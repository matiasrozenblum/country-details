package mrozenblum.repository

import mrozenblum.domain.response.countrydetails.CountryDetails
import mrozenblum.repository.rest.Callback

interface CountryDetailsRepository {
    fun details(code: String, callback: Callback<CountryDetails>)
}