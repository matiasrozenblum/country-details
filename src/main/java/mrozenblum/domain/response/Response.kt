package mrozenblum.domain.response

import org.joda.time.DateTime

data class Response(val country: String? = null,
                    val isoCode: String? = null,
                    val language: List<String> = listOf(),
                    val currency: String? = null,
                    val dateTimes: List<String> = listOf(),
                    val distance: String? = null)