//https://blog.jdriven.com/2023/03/gradle-goodness-publish-version-catalog-for-sharing-between-projects/
plugins {
    `version-catalog`
    `maven-publish`
    id("nebula.release") version "17.2.0"
}

group = "io.excalibur.version.catalogs"

repositories {
    mavenCentral()
}

catalog {
    versionCatalog {
        from(files("gradle/libs.versions.toml"))
    }
}

nebulaRelease {
    val releaseBranchPatterns = emptySet<String>()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // The version-catalog plugin adds a new component
            // "versionCatalog" that we can use a publication.
            // We will get a POM file and a generated version catalog
            // TOML file that are part of the publication.
            from(components["versionCatalog"])
        }
    }

    repositories {
        // Configuration for Maven repo to publish our version catalog to.
        maven {
            url = uri("https://maven.pkg.github.com/amoakoagyei/version-catalogs")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}