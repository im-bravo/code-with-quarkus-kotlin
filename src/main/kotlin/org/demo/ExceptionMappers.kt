package org.demo

import io.quarkus.arc.log.LoggerName
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.demo.predicate.IsBadRequestException
import org.jboss.logging.Logger

data class ErrorResponseBody(val message: String)

@Provider
class ExceptionMappers : ExceptionMapper<Throwable>  {

    @LoggerName("app")
    lateinit var log: Logger

    override fun toResponse(exception: Throwable?): Response {
        return if (exception != null && IsBadRequestException.test(exception)) {
            Response.status(Response.Status.BAD_REQUEST).build()
        } else {
            when (exception) {
                is BadRequestException -> {
                    Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ErrorResponseBody(exception.message!!))
                        .build()
                }
                is WebApplicationException -> {
                    Response
                        .status(exception.response.status)
                        .entity(ErrorResponseBody(exception.message ?: "Unexpected error"))
                        .build()
                }
                else -> {
                    log.error("Unexpected error", exception)
                    Response.serverError().build()
                }
            }
        }

    }
}