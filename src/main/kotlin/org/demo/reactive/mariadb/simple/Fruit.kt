package org.demo.reactive.mariadb.simple

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple


class Fruit {
    var id: Long? = null
    var name: String? = null

    constructor(name: String) {
        this.name = name
    }

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }

    fun save(client: MySQLPool): Uni<Long> {
        return client.preparedQuery("INSERT INTO fruits (name) VALUES (?) RETURNING id").execute(Tuple.of(name))
            .onItem().transform { rowSet -> rowSet.iterator().next().getLong("id") }
    }

    fun update(client: MySQLPool): Uni<Boolean> {
        return client.preparedQuery("UPDATE fruits SET name = ? WHERE id = ?").execute(Tuple.of(name, id))
            .onItem().transform { rowSet -> rowSet.rowCount() == 1 }
    }

    companion object {
        fun findAll(client: MySQLPool): Multi<Fruit> {
            return client.query("SELECT id, name FROM fruits ORDER BY name")
                .execute()
                .onItem().transformToMulti { set -> Multi.createFrom().iterable(set) }
                .onItem().transform { row: Row -> from(row) }
        }

        fun findById(client: MySQLPool, id: Long): Uni<Fruit> {
//            return Uni.createFrom().item(Fruit(id, "Apple"))
            return client.preparedQuery("SELECT id, name FROM fruits WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform { rowSet -> rowSet.iterator() }
                .onItem().transform { iterator -> if (iterator.hasNext()) from(iterator.next()) else null }
        }

        fun delete(client: MySQLPool, id: Long): Uni<Boolean> {
            return client.preparedQuery("DELETE FROM fruits WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform { rowSet -> rowSet.rowCount() == 1 }
        }

        private fun from(row: Row): Fruit {
            return Fruit(row.getLong("id"), row.getString("name"))
        }
    }
}