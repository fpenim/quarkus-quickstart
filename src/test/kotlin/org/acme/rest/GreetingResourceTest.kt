package org.acme.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import java.util.*

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/greeting")
          .then()
             .statusCode(200)
             .body(`is`("hello"))
    }

    @Test
    internal fun testGreetingEndpoint() {
        val uuid = UUID.randomUUID().toString()

        given()
            .pathParam("name", uuid)
            .`when`().get("/greeting/{name}")
            .then()
                .statusCode(200)
                .body(`is`("hello dear $uuid!"))
    }
}