plugins {
    java
    `maven-publish`
    signing
}

group = "com.hpfxd"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal() // for NMS
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.viaversion.com")
}

sourceSets {
    create("1.8.8") {}
    create("viaver") {}
    create("api") {}
}

dependencies {
    compileOnly(sourceSets["1.8.8"].output)
    compileOnly(sourceSets["viaver"].output)
    compileOnly(sourceSets["api"].output)
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // api
    "apiCompileOnly"("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // viaver
    "viaverImplementation"(sourceSets["api"].output)
    "viaverCompileOnly"("com.viaversion:viaversion-api:4.1.1")
    "viaverCompileOnly"("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // 1.8.8
    "188Implementation"(sourceSets["api"].output)
    "188CompileOnly"("org.github.paperspigot:paperspigot:1.8.8-R0.1-SNAPSHOT")
    "188CompileOnly"("org.jetbrains:annotations:23.0.0") // 1.8.8 doesn't have JB annotations
}

java {
    withSourcesJar()
}

tasks {
    named("jar", Jar::class.java) {
        from(sourceSets["api"].output, sourceSets["viaver"].output, sourceSets["1.8.8"].output)
    }

    named("sourcesJar", Jar::class.java) {
        from(sourceSets["api"].allSource, sourceSets["viaver"].allSource, sourceSets["1.8.8"].allSource)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
            }

            pom {
                url.set("https://hpfxd.com/")

                developers {
                    developer {
                        id.set("hpfxd")
                        name.set("Nate")
                        email.set("me@hpfxd.com")
                    }
                }
            }
        }
    }

    repositories {
        // only declare repository if credentials are provided
        if (findProperty("repository.hpfxd.username") != null) {
            maven {
                name = "hpfxd-repo"

                url = uri("https://repo.hpfxd.com/releases/")

                credentials {
                    username = property("repository.hpfxd.username") as String
                    password = property("repository.hpfxd.password") as String
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
    useGpgCmd()
}
