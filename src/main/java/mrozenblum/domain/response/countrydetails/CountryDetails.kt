package mrozenblum.domain.response.countrydetails

data class CountryDetails(val latlng: List<Float>,
                          val timezones: List<String>,
                          val currencies: List<Currency>,
                          val languages: List<Language>)