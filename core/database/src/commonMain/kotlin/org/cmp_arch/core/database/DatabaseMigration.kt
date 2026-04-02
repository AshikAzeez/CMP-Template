package org.cmp_arch.core.database

data class DatabaseMigration(
    val fromVersion: Int,
    val toVersion: Int,
    val description: String,
)

class MigrationRegistry(
    val schemaVersion: Int,
    private val migrations: List<DatabaseMigration>,
) {
    fun validate() {
        val sorted = migrations.sortedBy { it.fromVersion }
        var expectedFrom = 1
        sorted.forEach { migration ->
            require(migration.fromVersion == expectedFrom) {
                "Missing migration step from version $expectedFrom"
            }
            expectedFrom = migration.toVersion
        }

        require(expectedFrom == schemaVersion) {
            "Latest migration ends at $expectedFrom but schemaVersion is $schemaVersion"
        }
    }

    fun all(): List<DatabaseMigration> = migrations
}
