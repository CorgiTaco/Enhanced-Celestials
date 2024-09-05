import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    java
    idea
    `maven-publish`
}

val minecraftVersion = project.properties["minecraft_version"] as String
architectury.minecraft = minecraftVersion

allprojects {
    version = "${minecraftVersion}-${project.properties["version"]}"
    group = project.properties["group"] as String
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    base.archivesName.set(project.properties["archives_base_name"] as String + "-${project.name}")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
    loom.silentMojangMappingsLicense()
    loom.mixin.useLegacyMixinAp.set(false)

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.parchmentmc.org")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.jt-dev.tech/releases")
    }

    @Suppress("UnstableApiUsage")
    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"(loom.layered{
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$minecraftVersion:${project.properties["parchment"]}@zip")
        })

        compileOnly("org.jetbrains:annotations:24.1.0")
        compileOnly("com.google.auto.service:auto-service:1.1.1")
        annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(17)
    }

    publishing {
        publications.create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }

        repositories {
            mavenLocal()
            maven {
                val releasesRepoUrl = "https://maven.jt-dev.tech/releases"
                val snapshotsRepoUrl = "https://maven.jt-dev.tech/snapshots"
                url = uri(if (project.version.toString().endsWith("SNAPSHOT") || project.version.toString().startsWith("0")) snapshotsRepoUrl else releasesRepoUrl)
                name = "JTDev-Maven-Repository"
                credentials {
                    username = project.properties["repoLogin"]?.toString()
                    password = project.properties["repoPassword"]?.toString()
                }
            }
        }
    }
}
