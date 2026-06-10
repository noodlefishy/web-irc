plugins {
    kotlin("jvm") version "2.4.0"
    application
}

group = "io.cuttlefish"
version = "1.0-SNAPSHOT"
val ktorVersion = "3.5.0"
repositories {
    mavenCentral()
}
application {
    mainClass.set("io.cuttlefish.MainKt")
}

dependencies {
    implementation("io.ktor:ktor-server-core:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("io.ktor:ktor-server-websockets:${ktorVersion}")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}