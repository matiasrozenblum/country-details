package mrozenblum.controller

import mrozenblum.domain.exception.DestinationParamNotFoundException

object ParamParser {

    fun getParamFromUri(query: String?, paramName: String):String {
        query ?: throw DestinationParamNotFoundException()
        val params = query.split("?")
        val ipParam = params.filter { it.matches(Regex("$paramName=[^&?]*")) }[0].split("=")[1]
        return ipParam
    }
}