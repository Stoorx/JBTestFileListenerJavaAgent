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
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-commons:9.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "one.stoorx.listenerAgent.ListenerAgent",
            "Agent-Class" to "one.stoorx.listenerAgent.ListenerAgent",
            "Main-Class" to "one.stoorx.listenerAgent.loadingTool.AgentLoadingTool",
            "Can-Retransform-Classes" to "true"
        )
    }
}