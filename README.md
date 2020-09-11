# Quarkus Kotlin Quickstart Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `rest-kotlin-quickstart-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/rest-kotlin-quickstart-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/rest-kotlin-quickstart-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

## Running PostgresDB
```
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 \
           --name postgres-quarkus-reactive -e POSTGRES_USER=quarkus_test \
           -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test \
           -p 5432:5432 postgres:11.2
```