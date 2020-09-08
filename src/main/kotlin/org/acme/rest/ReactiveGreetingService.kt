package org.acme.rest

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.time.Duration
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ReactiveGreetingService {

    fun greeting(name: String): Uni<String> = Uni.createFrom()
            .item(name)
            .onItem().transform { n -> "hello $n" }

    fun greetings(count: Long, name: String): Multi<String> = Multi.createFrom()
            .ticks().every(Duration.ofSeconds(1))
            .onItem().transform { n -> "hello $name ($n)" }
            .transform().byTakingFirstItems(count)
}