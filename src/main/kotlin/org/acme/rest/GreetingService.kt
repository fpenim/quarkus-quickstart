package org.acme.rest

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GreetingService {

    fun greeting(name: String) = "hello dear $name!"
}