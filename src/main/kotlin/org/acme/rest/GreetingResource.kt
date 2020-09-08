package org.acme.rest

import org.jboss.resteasy.annotations.SseElementType
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/greeting")
class GreetingResource(
        val greetingService: GreetingService,
        val reactiveGreetingService: ReactiveGreetingService
) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "hello"

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{name}")
    fun greeting(@PathParam(value = "name") name: String) = greetingService.greeting(name)

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/reactive/{name}")
    fun reactiveGreeting(@PathParam(value = "name") name: String) = reactiveGreetingService.greeting(name)

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/reactive/{count}/{name}")
    fun reactiveGreetings(@PathParam(value = "count") count: Long, @PathParam(value = "name") name: String) =
            reactiveGreetingService.greetings(count, name)

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    @Path("/reactive/stream/{count}/{name}")
    fun reactiveGreetingsAsStream(@PathParam(value = "count") count: Long, @PathParam(value = "name") name: String) =
            reactiveGreetingService.greetings(count, name)

}
