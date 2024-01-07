package org.demo.reactive.mariadb.simple

import io.quarkus.runtime.StartupEvent
import io.vertx.mutiny.mysqlclient.MySQLPool
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.eclipse.microprofile.config.inject.ConfigProperty


@ApplicationScoped
class DBInit(
    private val client: MySQLPool,
    @ConfigProperty(name = "myapp.schema.create") private val schemaCreate: Boolean,
) {
    fun onStart(@Observes ev: StartupEvent?) {
        if (schemaCreate) {
            initdb()
        }
    }

    private fun initdb() {
        client.query("DROP TABLE IF EXISTS fruits").execute()
            .flatMap { _ -> client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Kiwi')").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Durian')").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Pomelo')").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Lychee')").execute() }
            .await().indefinitely()
    }
}