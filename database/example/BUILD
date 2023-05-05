load("//database:schema.bzl", "schema_bundle")
load("//database:psql.bzl", "postgres_schema")

schema_bundle(
    name = "example_schema",
    proto_libs = [
        "//proto/example:user_proto",
    ],
    schema_input = "example_schema.textproto",
)

# Can create a Postgres database named "FooDatabase" like this:
# ./database/bin/psql_schema FooDatabase //database/example:psql_example_schema | psql -U postgres
postgres_schema(
    name = "postgres",
    input_bundle = ":example_schema",
)
