plugins {
    id("dev.kikugie.loom-back-compat")
    id("dev.kikugie.stonecutter")
	`maven-publish`
}

version = providers.gradleProperty("mod_version").get() + "+${sc.current.version}"
group = providers.gradleProperty("maven_group").get()

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
    maven { url = uri("https://repo.repsy.io/kr1v/maven/") }
    maven { url = uri("https://masa.dy.fi/maven/sakura-ryoko/") }
    maven { url = uri("https://maven.fallenbreath.me/releases") }
    maven { url = uri("https://maven.terraformersmc.com/") }
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("world-panorama") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    loomx.applyMojangMappings()

	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")

    modImplementation("fi.dy.masa.malilib:malilib-fabric-${project.property("malilib_version")}")
    modImplementation("net.kr1v:malilib-api:${project.property("malilib_api_version")}")
    annotationProcessor("net.kr1v:malilib-api-processor:1.0.0")

    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}

val javaVersion = when (sc.current.parsed.matches(">=26.1")) {
    true -> { JavaVersion.VERSION_25 }
    false -> { JavaVersion.VERSION_21 }
}

tasks.processResources {
	val version = version
	val javaVersion = javaVersion.majorVersion
    var minecraftVersionRange = when (sc.current.version) {
        "26.1.2" -> {
            "~26.1"
        }
        "1.21.11" -> {
            "1.21.11"
        }
        else -> {
            throw GradleException("Version not specified!")
        }
    }
	inputs.property("version", version)
	inputs.property("java_version", javaVersion)
	inputs.property("minecraft_version_range", minecraftVersionRange)

	filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to version,
                "java_version" to javaVersion,
                "minecraft_version_range" to minecraftVersionRange
            )
        )
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = javaVersion.majorVersion.toInt()
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
