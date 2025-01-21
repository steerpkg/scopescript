plugins {
    id("java")
    id("maven-publish")
    id("signing")
}

group = "org.steerpkg"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.steerpkg"
            artifactId = "scopescript"
            version = "0.1.0"
        }
    }

    repositories {
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
            credentials {
                username = (project.findProperty("mavenUsername") ?: System.getenv("MAVEN_USERNAME")).toString()
                password = (project.findProperty("mavenPassword") ?: System.getenv("MAVEN_PASSWORD")).toString()
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}