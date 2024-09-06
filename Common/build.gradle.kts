architectury {
    common("forge", "fabric", "neoforge")
    platformSetupLoomIde()
}

val minecraftVersion = project.properties["minecraft_version"] as String

loom.accessWidenerPath.set(file("src/main/resources/enhancedcelestials.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")

    modCompileOnly("corgitaco.corgilib:Corgilib-Fabric:$minecraftVersion-${project.properties["corgilib_version"]}")
}