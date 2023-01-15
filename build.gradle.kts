plugins {
    java
    id("io.freefair.lombok") version "6.6.1"
}

group = "one.stoorx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "one.stoorx.listenerAgent.StubAgent",
            "Agent-Class" to "one.stoorx.listenerAgent.StubAgent",
            "Main-Class" to "one.stoorx.listenerAgent.loadingTool.AgentLoadingTool"
        )
    }
}