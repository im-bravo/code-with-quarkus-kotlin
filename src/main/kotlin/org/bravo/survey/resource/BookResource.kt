package org.bravo.survey.resource

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.common.annotation.NonBlocking
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.bravo.survey.entity.Books
import org.bravo.survey.repository.BookRepository


@Path("hibernate/books")
class BookResource {

    @Inject
    lateinit var bookRepository: BookRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getBooks(): Uni<MutableList<Books>> {
        return bookRepository.listAll()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getBookById(@PathParam("id") id: Long): Uni<Response> {
        return bookRepository.findById(id)
            .onItem().transform { book ->
                if (book != null) Response.ok(book) else Response.status(Response.Status.NOT_FOUND)
            }
            .onItem().transform { obj: Response.ResponseBuilder -> obj.build() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    fun createBook(book: Books): Uni<Books> {
        return bookRepository.persist(book)
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateBook(@PathParam("id") id: Long, book: Books): Uni<Response> {
        return Panache.withTransaction { bookRepository.updateBook(id, book) }
            .onItem().transform { updatedBook: Books -> Response.ok(updatedBook) }
            .onItem().transform { obj: Response.ResponseBuilder -> obj.build() }
    }

//    @DELETE
//    @Path("/{id}")
//    @Transactional
//    fun deleteBook(@PathParam("id") id: Long): Uni<Response> {
//        return bookRepository.deleteById(id).onItem().transform { deleted: Boolean ->
//            if (deleted) Response.Status.NO_CONTENT else Response.Status.NOT_FOUND
//        }.onItem().transform { status: Response.Status -> Response.status(status).build() }
//    }
}