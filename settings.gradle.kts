pluginManagement {
	repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
	}
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.5"
    id("dev.kikugie.loom-back-compat") version "0.3"
}

// Should match your modid
rootProject.name = "world-panorama"

stonecutter {
    create(rootProject) {
        versions("1.21.11", "26.1.2")
        vcsVersion = "26.1.2"
    }
}