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
    "mysqlConnectorRuntime" to "8.4.0",
    "hibernate-core" to "6.4.4.Final",
    "flyway" to "10.21.0"
)

dependencies {
    implementation("org.hibernate:hibernate-core:${versions["hibernate-core"]}")
    implementation("org.flywaydb:flyway-core:${versions["flyway"]}")
    implementation("org.flywaydb:flyway-mysql:${versions["flyway"]}")

    implementation("com.mysql:mysql-connector-j:${versions["mysqlConnector"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${versions["junit"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${versions["junit"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${versions["junitPlatform"]}")
    testImplementation("org.mockito:mockito-core:${versions["mockito"]}")
    testImplementation("org.mockito:mockito-junit-jupiter:${versions["mockito"]}")
    testImplementation("net.bytebuddy:byte-buddy:${versions["byteBuddy"]}")
    testImplementation("net.bytebuddy:byte-buddy-agent:${versions["byteBuddy"]}")
}

tasks.test {
    useJUnitPlatform()
}