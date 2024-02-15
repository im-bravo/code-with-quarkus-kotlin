package org.bravo.survey.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.logging.Logger
import ulid.UlidMonotonicFactory

@Path("/ulid")
class UlidController(
    val ulidMonotonicFactory: UlidMonotonicFactory
) {
    private val LOG: Logger  = Logger.getLogger(UlidController::class.java)

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun gen() = ulidMonotonicFactory.nextULID().also {
        LOG.info("Generated ULID: $it -> ${it.timestamp}")
    }

    @GET
    @Path("/{num}")
    @Produces(MediaType.APPLICATION_JSON)
    fun genMultiple(@PathParam("num") num: Int) = mutableListOf<String>().let { list ->
        repeat(num) {
            list.add(ulidMonotonicFactory.nextULID().toString())
        }
        list
    }
}