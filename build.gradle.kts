plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "io.comeandcommue"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/tmdghks2515/comeandcommue.lib.web-lib")
        credentials {
            // gradle.properties의 gpr.user / gpr.key 사용
			username = (findProperty("gpr.user") as String?) ?: System.getenv("GITHUB_ACTOR")
			password = (findProperty("gpr.key") as String?) ?: System.getenv("GITHUB_TOKEN")
        }
    }
	maven {
		name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/tmdghks2515/comeandcommue.lib.data-lib")
		credentials {
			username = (findProperty("gpr.user") as String?) ?: System.getenv("GITHUB_ACTOR")
			password = (findProperty("gpr.key") as String?) ?: System.getenv("GITHUB_TOKEN")
		}
	}
	mavenLocal()
}

dependencies {
	implementation("io.comeandcommue.lib:web-lib:0.0.1-SNAPSHOT")
	implementation("io.comeandcommue.lib:data-lib:0.0.1-SNAPSHOT")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.auth0:java-jwt:4.4.0")
	runtimeOnly("org.postgresql:postgresql")
	compileOnly("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
