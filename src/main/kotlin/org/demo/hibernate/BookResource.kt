package org.demo.hibernate

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("hibernate/books")
class BookResource {

    @Inject
    lateinit var bookRepository: BookRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getBooks(): List<Books> {
        return bookRepository.listAll()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getBookById(@PathParam("id") id: Long): Books? {
        return bookRepository.findById(id)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun createBook(book: Books): Books {
        return bookRepository.persist(book).let { book }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateBook(@PathParam("id") id: Long, book: Books): Books {
        val existingBook = bookRepository.findById(id) ?: throw NotFoundException("Book with id $id not found")
        existingBook.name = book.name
        bookRepository.persist(existingBook)
        return existingBook
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteBook(@PathParam("id") id: Long) {
        bookRepository.deleteById(id)
    }
}