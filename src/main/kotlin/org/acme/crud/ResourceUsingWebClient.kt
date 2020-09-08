package org.acme.crud

import io.smallrye.mutiny.Uni
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.ext.web.client.WebClient
import javax.annotation.PostConstruct
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/fruit-data")
class ResourceUsingWebClient(val vertx: Vertx) {

    private lateinit var client: WebClient

    @PostConstruct
    fun initialize() {
        this.client = WebClient.create(
                vertx,
                WebClientOptions()
                        .setDefaultHost("fruityvice.com")
                        .setDefaultPort(443).setSsl(true)
                        .setTrustAll(true)
        )
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    fun getFruitData(@PathParam("name") name: String): Uni<JsonObject> = client
            .get("/api/fruit/$name")
            .send()
            .map { response ->
                if (response.statusCode() == Response.Status.OK.statusCode) {
                    response.bodyAsJsonObject()
                } else {
                    JsonObject()
                            .put("code", response.statusCode())
                            .put("message", response.bodyAsString())
                }
            }
}