package org.demo

import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.demo.predicate.IsBadRequestException

data class ErrorResponseBody(val message: String)

@Provider
class ExceptionMappers : ExceptionMapper<Throwable>  {
    override fun toResponse(p0: Throwable?): Response {
        return if (p0 != null && IsBadRequestException.test(p0)) {
            Response.status(Response.Status.BAD_REQUEST).build()
        } else {
            when (p0) {
                is BadRequestException -> {
                    Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ErrorResponseBody(p0.message!!))
                        .build()
                }
                else -> Response.serverError().build()
            }
        }

    }

}