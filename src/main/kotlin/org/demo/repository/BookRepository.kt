package org.demo.repository

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotFoundException
import org.demo.entity.Books


@ApplicationScoped
class BookRepository : PanacheRepository<Books> {
    fun findByName(name: String): Uni<Books> {
        return find("name", name).firstResult()
    }

    fun updateBook(id: Long, updatedBook: Books): Uni<Books> {
        return findById(id)
            .onItem().ifNull().failWith(NotFoundException("Book not found"))
            .map { existingBook ->
                // Update fields selectively (avoiding unnecessary updates)
                existingBook.name = updatedBook.name
                // ... update other fields as needed
                existingBook
            }
            .flatMap { filledBook -> persist(filledBook) }
    }
}