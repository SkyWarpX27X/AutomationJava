plugins {
    id("java")
    id("java-gradle-plugin")
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
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins{
        create("formattingGradlePlugin") {
            id = "org.example.formatting"
            implementationClass = "FormattingPlugin"
        }
    }
}
