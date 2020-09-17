package org.acme.crud

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URI
import javax.annotation.PostConstruct
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FruitResource(
        private val client: PgPool,
        private val repository: FruitRepository,

        @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
        private val schemaCreate: Boolean
) {
    @PostConstruct
    fun config(){
        initDataBase()
    }

    private fun initDataBase() {
        client.query("DROP TABLE IF EXISTS fruits").execute()
                .flatMap { client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute() }
                .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Kiwi')").execute() }
                .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Durian')").execute() }
                .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Pomelo')").execute() }
                .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Lychee')").execute() }
                .await().indefinitely()
    }

    @GET
    fun getFruits(): Multi<Fruit> = repository.findAll()

    @GET
    @Path("{id}")
    fun getFruit(@PathParam("id") id: Long): Uni<Response> = repository.findById(id)
            .onItem().transform { fruit -> if (fruit != null) Response.ok(fruit) else Response.status(Response.Status.NOT_FOUND) }
            .onItem().transform { responseBuilder -> responseBuilder.build() }

    @POST
    fun createFruit(fruit: Fruit): Uni<Response> = repository.save(fruit)
            .onItem().transform { id -> URI.create("/fruits/$id") }
            .onItem().transform { uri -> Response.created(uri).build() }

    @PUT
    @Path("{id}")
    fun updateFruit(@PathParam("id") id: Long, fruit: Fruit): Uni<Response> = repository.update(id, fruit)
            .onItem().transform { updated -> if (updated) Response.Status.OK else Response.Status.NOT_FOUND }
            .onItem().transform { status -> Response.status(status).build() }

    @DELETE
    @Path("{id}")
    fun deleteFruit(@PathParam("id") id: Long): Uni<Response> = repository.delete(id)
            .onItem().transform { deleted -> if (deleted) Response.Status.NO_CONTENT else Response.Status.NOT_FOUND }
            .onItem().transform { status -> Response.status(status).build() }
}
