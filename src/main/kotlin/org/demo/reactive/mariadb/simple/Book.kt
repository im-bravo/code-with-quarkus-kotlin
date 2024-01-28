package org.demo.reactive.mariadb.simple

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable


@Serializable
class Book {
    var id: Long? = null
    @field:NotBlank(message = "Fruit name must not be blank")
    var name: String? = null

    constructor(name: String) {
        this.name = name
    }

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }

    fun save(client: MySQLPool): Uni<Long> {
        return client.preparedQuery("INSERT INTO books (name) VALUES (?) RETURNING id").execute(Tuple.of(name))
            .onItem().transform { rowSet -> rowSet.iterator().next().getLong("id") }
    }

    fun update(client: MySQLPool): Uni<Boolean> {
        return client.preparedQuery("UPDATE books SET name = ? WHERE id = ?").execute(Tuple.of(name, id))
            .onItem().transform { rowSet -> rowSet.rowCount() == 1 }
    }

    companion object {
        fun findAll(client: MySQLPool): Multi<Book> {
            return client.query("SELECT id, name FROM books ORDER BY name")
                .execute()
                .onItem().transformToMulti { set -> Multi.createFrom().iterable(set) }
                .onItem().transform { row: Row -> from(row) }
        }

        fun findById(client: MySQLPool, id: Long): Uni<Book> {
//            return Uni.createFrom().item(Fruit(id, "Apple"))
            return client.preparedQuery("SELECT id, name FROM books WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform { rowSet -> rowSet.iterator() }
                .onItem().transform { iterator -> if (iterator.hasNext()) from(iterator.next()) else null }
        }

        fun delete(client: MySQLPool, id: Long): Uni<Boolean> {
            return client.preparedQuery("DELETE FROM books WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform { rowSet -> rowSet.rowCount() == 1 }
        }

        private fun from(row: Row): Book {
            return Book(row.getLong("id"), row.getString("name"))
        }
    }
}