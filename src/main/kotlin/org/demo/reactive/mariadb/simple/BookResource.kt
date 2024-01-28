package org.demo.reactive.mariadb.simple

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.mysqlclient.MySQLPool
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolation
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.ResponseBuilder
import jakarta.ws.rs.core.Response.Status
import org.demo.ErrorResponseBody
import org.demo.predicate.IsBadRequestException

@Path("/reactive/books")
class FruitResource() {

    @Inject
    lateinit var client: MySQLPool

    @GET
    fun get(): Multi<Book> {
        return Book.findAll(client)
    }

    @GET
    @Path("{id}")
    fun getSingle(id: Long): Uni<Response> {
        return Book.findById(client, id)
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
    fun create(@Valid fruit: NewBookRequest): Uni<Response> {
        return Book(
            name = fruit.name ?: throw BadRequestException("Hm...validation not works")
        ).save(client)
            .onItem().transformToUni { id: Long ->
                // -- fetch from DB
                // Sometime it's not a good idea for example your database is using read/write separations
                // or use ProxySQL on the top of your MySQL/MariaDB cluster etc.
                //Fruit.findById(client, id)
                //    .onItem().transform { fruit: Fruit ->
                //        Response.ok().entity(fruit).build()
                //    }

                // -- build
                Uni.createFrom().item(
                    Response.ok().entity(Book(
                        id = id,
                        name = fruit.name
                    )).build()
                )
            }
    }

    @PUT
    @Path("{id}")
    fun update(id: Long?, book: Book): Uni<Response> {
        println("id: $id")
        println("book: ${book.id}, ${book.name}")
        return book.update(client)
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
            .onFailure().recoverWithItem{ throwable: Throwable? ->
                Response.status(
                    Status.INTERNAL_SERVER_ERROR
                ).entity(ErrorResponseBody(throwable?.message ?: "Unknown error")).build()}
    }

    @DELETE
    @Path("{id}")
    fun delete(id: Long): Uni<Response> {
        return Book.delete(client, id)
            .onItem().transform { deleted -> if (deleted) Status.NO_CONTENT else Status.NOT_FOUND }
            .onItem().transform { status -> Response.status(status).build() }
    }
}