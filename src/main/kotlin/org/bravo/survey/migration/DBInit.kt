package org.bravo.survey.migration

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
            initDb()
        }
    }

    private fun initDb() {
        dropTables()
        createTables()
//        insertData()
//        client.query("DROP TABLE IF EXISTS books").execute()
//            .flatMap { _ -> client.query("CREATE TABLE books (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute() }
//            .flatMap { _ -> client.query("INSERT INTO books (name) VALUES ('Kiwi')").execute() }
//            .flatMap { _ -> client.query("INSERT INTO books (name) VALUES ('Durian')").execute() }
//            .flatMap { _ -> client.query("INSERT INTO books (name) VALUES ('Pomelo')").execute() }
//            .flatMap { _ -> client.query("INSERT INTO books (name) VALUES ('Lychee')").execute() }
//            .await().indefinitely()
    }

    private fun dropTables() {
        client.query("DROP TABLE IF EXISTS survey").execute()
            .await().indefinitely()
    }

    private fun createTables() {
        client.query(CREATE_SURVEY_TABLE).execute()
            .await().indefinitely()
    }

    companion object SQLStatements {
        const val CREATE_SURVEY_TABLE = """
            CREATE TABLE IF NOT EXISTS survey (
                id binary(16) PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
        """
    }
//
//    private fun createSurveyTable() {
//        client.query("CREATE TABLE IF NOT EXISTS survey (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute()
//            .await().indefinitely()
//    }


}