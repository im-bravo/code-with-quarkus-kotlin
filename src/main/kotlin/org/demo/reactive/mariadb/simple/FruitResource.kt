package org.demo.reactive.mariadb.simple

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.mysqlclient.MySQLPool
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolation
import jakarta.validation.Valid
import jakarta.validation.Validator
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.ResponseBuilder
import jakarta.ws.rs.core.Response.Status
import org.demo.predicate.IsBadRequestException
import java.net.URI
import java.util.stream.Collectors


class Result {
    internal constructor(message: String) {
        this.isSuccess = true
        this.message = message
    }

    internal constructor(violations: Set<ConstraintViolation<*>?>) {
        this.isSuccess = false
        this.message = violations.stream()
            .map { cv: ConstraintViolation<*>? -> cv!!.message }
            .collect(Collectors.joining(", "))
    }

    var message: String
        private set
    var isSuccess: Boolean
        private set
}

@Path("fruits")
class FruitResource() {

    @Inject
    lateinit var client: MySQLPool
//    @Inject
//    lateinit var validator: Validator

    //private val client: MySQLPool = client

    @GET
    fun get(): Multi<Fruit> {
        return Fruit.findAll(client)
    }

    @GET
    @Path("{id}")
    fun getSingle(id: Long): Uni<Response> {
        return Fruit.findById(client, id)
            .onItem().transform { fruit ->
                if (fruit != null) Response.ok(fruit) else Response.status(
                    Status.NOT_FOUND
                )
            }
            .onItem().transform { obj: ResponseBuilder -> obj.build() }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(@Valid fruit: NewFruitRequest): Uni<Response> {
        return Fruit(
            name = fruit.name ?: throw BadRequestException("Hm...validation not works")
        ).save(client)
            .onItem().transformToUni { id: Long ->
                // -- fetch from DB
                //Fruit.findById(client, id)
                //    .onItem().transform { fruit: Fruit ->
                //        Response.ok().entity(fruit).build()
                //    }

                // -- build
                Uni.createFrom().item(
                    Response.ok().entity(Fruit(
                        id = id,
                        name = fruit.name
                    )).build()
                )
            }
    }

    @PUT
    @Path("{id}")
    fun update(id: Long?, fruit: Fruit): Uni<Response> {
        return fruit.update(client)
            .onItem().transform { updated: Boolean -> if (updated) Status.OK else Status.NOT_FOUND }
            .onItem().transform { status: Status? ->
                Response.status(
                    status
                ).build()
            }
            .onFailure(IsBadRequestException).recoverWithItem(
                Response.status(
                    Status.BAD_REQUEST
                ).build()
            )
    }

    @DELETE
    @Path("{id}")
    fun delete(id: Long): Uni<Response> {
        return Fruit.delete(client, id)
            .onItem().transform { deleted -> if (deleted) Status.NO_CONTENT else Status.NOT_FOUND }
            .onItem().transform { status -> Response.status(status).build() }
    }
}