plugins {
    id("java")
    id("io.freefair.lombok") version "9.1.0"
}

group = "kz.shyngys"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val versions = mapOf(
    "junit" to "5.11.4",
    "junitPlatform" to "1.11.4",
    "mockito" to "5.14.2",
    "byteBuddy" to "1.15.11",
    "mysqlConnector" to "8.2.0",
    "liquibaseCore" to "4.25.1",
    "liquibaseRuntime" to "4.23.2",
    "mysqlConnectorRuntime" to "8.4.0",
    "snakeyaml" to "2.2",
    "hibernate-core" to "6.4.4.Final"
)

dependencies {
    implementation("org.hibernate:hibernate-core:${versions["hibernate-core"]}")

    implementation("com.mysql:mysql-connector-j:${versions["mysqlConnector"]}")
    implementation("org.liquibase:liquibase-core:${versions["liquibaseCore"]}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}