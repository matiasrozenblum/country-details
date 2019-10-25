package mrozenblum.controller

import com.sun.net.httpserver.HttpExchange
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class ParamParserTest{
    @Test
    fun getParamFromUri(){
        val parser = ParamParser
        val result = parser.getParamFromUri("localhost:9290/destinations?destination=buenosaires", "destination")
        Assert.assertEquals("buenosaires", result)
    }
}