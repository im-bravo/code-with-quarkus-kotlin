package org.bravo.survey.controller

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import ulid.ULID

@Path("/ulid")
class UlidController {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun gen() = listOf(
        ULID.randomULID()
    )

    @GET
    @Path("/{num}")
    @Produces(MediaType.APPLICATION_JSON)
    fun genMultiple(@PathParam("num") num: Int) = mutableListOf<String>().let { list ->
        repeat(num) {
            list.add(ULID.randomULID())
        }
        list
    }
}