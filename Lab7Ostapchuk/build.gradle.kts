val mockitoAgent: Configuration by configurations.creating

plugins {
    id("java")
    id("info.solidsoft.pitest") version "1.19.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.junit.platform:junit-platform-suite-api")
    testRuntimeOnly("org.junit.platform:junit-platform-suite-engine")
    mockitoAgent("org.mockito:mockito-core:5.14.2") {
        isTransitive = false
    }
}

tasks.test {
    useJUnitPlatform()
    doFirst {
        val agentPath = mockitoAgent.asPath
        jvmArgs("-javaagent:$agentPath")
    }
}

pitest {
    targetClasses.set(listOf("org.example.*"))
    targetTests.set(listOf("org.example.*"))
    junit5PluginVersion.set("1.2.3")
    threads.set(4)
    outputFormats.set(listOf("HTML"))
    timestampedReports.set(false)
    excludedMethods.set(listOf("get*", "set*"))
    jvmArgs.set(listOf("-javaagent:${mockitoAgent.asPath}"))
}