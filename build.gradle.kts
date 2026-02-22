plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("java-library")
    id("maven-publish")
}

group = "com.fancia.backend"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

fun RepositoryHandler.codeArtifactRepo(repoName: String) {
    maven {
        val baseUrl = System.getenv("ARTIFACT_REPO_URL")
            ?: project.findProperty("ARTIFACT_REPO_URL") as String?
            ?: error("ARTIFACT_REPO_URL must be provided via property or environment variable")

        url = uri("$baseUrl/$repoName/")
        credentials {
            username = System.getenv("ARTIFACT_REPO_USER") ?: (project.findProperty("ARTIFACT_REPO_USER") as String?)
            password =
                System.getenv("ARTIFACT_REPO_PASSWORD") ?: (project.findProperty("ARTIFACT_REPO_PASSWORD") as String?)
        }
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    isEnabled = false
}

tasks.jar {
    isEnabled = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.fancia.backend.shared"
            artifactId = "common"
            version = "0.0.1-SNAPSHOT"
            from(components["java"])
        }
    }
    repositories {
        maven {
            val domain = System.getenv("CODEARTIFACT_DOMAIN")
            val account = System.getenv("ACCOUNT_ID")
            val region = System.getenv("AWS_REGION")
            val repo = System.getenv("CODEARTIFACT_REPO")
            url = uri("https://$domain-$account.d.codeartifact.$region.amazonaws.com/maven/$repo/")
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}