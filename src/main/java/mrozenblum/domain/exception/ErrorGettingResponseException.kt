package mrozenblum.domain.exception

import java.lang.RuntimeException

class ErrorGettingResponseException(error: Throwable): RuntimeException(error)