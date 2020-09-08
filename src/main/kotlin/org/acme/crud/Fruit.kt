package org.acme.crud

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.RowSet
import io.vertx.mutiny.sqlclient.Tuple
import org.acme.crud.Fruit.Companion.from
import java.util.stream.StreamSupport
import javax.enterprise.context.ApplicationScoped

data class Fruit(val id: Long?, val name: String) {
    companion object {
        fun from(row: Row): Fruit = Fruit(row.getLong("id"), row.getString("name"))
    }
}

@ApplicationScoped
class FruitService(
        private val client: PgPool
) {

    fun findAll(): Multi<Fruit> = client
            .query("SELECT id, name FROM fruits ORDER BY name ").execute()
            .onItem().transformToMulti { rowSet ->
                Multi.createFrom().items(
                        StreamSupport.stream(rowSet.spliterator(), false)
                )
            }.onItem().transform { row -> from(row) }

    fun findById(id: Long): Uni<Fruit?> = client
            .preparedQuery("SELECT id, name FROM fruits WHERE id = $1").execute(Tuple.of(id))
            .onItem().transform(RowSet<Row>::iterator)
            .onItem().transform { iterator -> if (iterator.hasNext()) from(iterator.next()) else null }

    fun save(fruit: Fruit): Uni<Long> = client
            .preparedQuery("INSERT INTO fruits (name) VALUES ($1) RETURNING (id)").execute(Tuple.of(fruit.name))
            .onItem().transform{ rowSet -> rowSet.iterator().next().getLong("id")  }

    fun update(id: Long, fruit: Fruit): Uni<Boolean> = client
            .preparedQuery("UPDATE fruits SET name = $1 WHERE id = $2").execute(Tuple.of(fruit.name, id))
            .onItem().transform { rowSet -> rowSet.rowCount() == 1 }

    fun delete(id: Long): Uni<Boolean> = client
            .preparedQuery("DELETE FROM fruits WHERE id = $1").execute(Tuple.of(id))
            .onItem().transform { rowSet -> rowSet.rowCount() == 1 }


}
