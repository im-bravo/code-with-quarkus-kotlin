package org.demo.hibernate

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
class BookRepository : PanacheRepository<Books>