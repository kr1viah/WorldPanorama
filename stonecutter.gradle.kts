plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "2.0.0"
}

stonecutter active "1.21.11"

publishMods {
    if (providers.environmentVariable("RELEASE_MODRINTH").orNull?.toBoolean() ?: false) {
        val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
        if (!modrinthToken.isPresent || modrinthToken.get() == "") {
            throw GradleException("Missing MODRINTH_TOKEN")
        }
    }
}