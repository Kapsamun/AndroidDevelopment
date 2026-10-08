pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieProject"
include(":app")
include(":domain")
include(":data")
include(":ScrollViewApp")
include(":ListViewApp")
include(":RecyclerViewApp")
include(":RetroFitApp")
include(":myapplication")
include(":fragmentapp")
include(":fragmentmanagerapp")
include(":resultapifragmentapp")
