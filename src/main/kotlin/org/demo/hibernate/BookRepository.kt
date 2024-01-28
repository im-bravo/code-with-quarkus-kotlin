package org.demo.hibernate

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
class BookRepository : PanacheRepository<Books> {
    fun findByName(name: String): Books? {
        return find("name", name).firstResult()
    }
}