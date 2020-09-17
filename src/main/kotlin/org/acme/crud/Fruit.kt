package org.acme.crud

import io.vertx.mutiny.sqlclient.Row

data class Fruit(
    val id: Long?,
    val name: String
) {

    companion object {
        fun from(row: Row): Fruit = Fruit(row.getLong("id"), row.getString("name"))
    }
}
